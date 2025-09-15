package com.apinayami.demo.service.Impl;

import com.apinayami.demo.dto.request.BillRequestDTO;
import com.apinayami.demo.dto.request.CartPaymentDTO;
import com.apinayami.demo.dto.response.*;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.exception.ResourceNotFoundException;
import com.apinayami.demo.mapper.AddressMapper;
import com.apinayami.demo.mapper.BillMapper;
import com.apinayami.demo.mapper.CartItemMapper;
import com.apinayami.demo.model.*;
import com.apinayami.demo.repository.*;
import com.apinayami.demo.service.IBillService;
import com.apinayami.demo.util.Decorator.CouponDecorator;
import com.apinayami.demo.util.Enum.EBillStatus;
import com.apinayami.demo.util.Enum.EPaymentMethod;
import com.apinayami.demo.util.Enum.EPaymentStatus;
import com.apinayami.demo.util.Strategy.OnlineBankingPaymentStrategy;
import com.apinayami.demo.util.Strategy.PaymentStrategy;
import com.apinayami.demo.util.Strategy.PaymentStrategyFactory;
import com.google.api.client.util.ArrayMap;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.PaymentLinkData;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BillServiceImpl implements IBillService {
    private final IBillRepository billRepository;
    private final IUserRepository userRepository;
    private final IProductRepository productRepository;
    private final IShippingRepository shippingRepository;
    private final ICouponRepository couponRepository;
    private final ISerialProductRepository serialProductRepository;
    private final IPaymentRepository paymentRepository;
    private final ICartRepository cartRepository;
    private final ICartItemRepository cartItemRepository;
    private final IAddressRepository addressRepository;
    private final ILineItemRepository lineItemRepository;
    private final BillMapper billMapper;
    private final PaymentStrategyFactory paymentStrategyFactory;
    private final CartItemMapper cartItemMapper;
    private final AddressMapper addressMapper;
    private final EmailService emailService;

    @Value("${payos.client-id}")
    private String clientId;

    @Value("${payos.api-key}")
    private String apiKey;

    @Value("${payos.checksum-key}")
    private String checksumKey;

    @Transactional
    public BillResponseDTO getBill(String email, CartPaymentDTO billRequestDTO) {
        UserModel customer = userRepository.getUserByEmail(email);
        if (customer == null) {
            throw new ResourceNotFoundException("User is empty");
        }
        BillResponseDTO bill = new BillResponseDTO();

        CartModel cart = cartRepository.findByCustomerModel(customer)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user"));

        List<CartItemModel> cartItems = cart.getCartItems().stream()
                .filter(item -> billRequestDTO.getCartId().contains(item.getId()))
                .collect(Collectors.toList());

        if (cartItems.isEmpty()) {
            throw new ResourceNotFoundException("No cart items found with the provided IDs");
        }

        bill.setListCartItem(cartItems.stream()
                .map(cartItemMapper::toCartItemDTO)
                .collect(Collectors.toList()));

        List<AddressModel> address = addressRepository.findByCustomerModel_IdAndActiveTrue(customer.getId());

        bill.setListAddress(address.stream()
                .map(addressMapper::toResponseDTO)
                .collect(Collectors.toList()));

        bill.setCoupon(billRequestDTO.getCouponId());
        bill.setDiscount(billRequestDTO.getDiscount());
        return bill;
    }

    @Transactional
    public Object createBill(String email, BillRequestDTO request) {
        UserModel customer = userRepository.getUserByEmail(email);
        if (customer == null) {
            throw new ResourceNotFoundException("User is empty");
        }

        AddressModel address = addressRepository.findById(request.getAddressId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Address not found with id: " + request.getAddressId()));
        ShippingModel shipping = ShippingModel.builder()
                .shippingFee(request.getShippingFee())
                .addressModel(address)
                .build();
        shippingRepository.save(shipping);

        CouponModel coupon = null;
        if (request.getCouponId() != null) {
            coupon = couponRepository.findByIdAndActiveTrue(request.getCouponId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Coupon not found with id: " + request.getCouponId()));
        }

        PaymentStrategy paymentStrategy = paymentStrategyFactory.getStrategy(request.getPaymentMethod().name());
        PaymentModel payment = paymentRepository.save(paymentStrategy.processPayment(request));
        BillModel bill = BillModel.builder()
                .totalPrice(123241.1)
                .discount(request.getDiscount())
                .paymentMethod(request.getPaymentMethod())
                .customerModel(customer)
                .shippingModel(shipping)
                .couponModel(coupon)
                .status(EBillStatus.PENDING)
                .paymentModel(payment)
                .build();

        CartModel cart = cartRepository.findByCustomerModel(customer)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user"));

        List<CartItemModel> cartItems = cart.getCartItems().stream()
                .filter(item -> request.getCartId().contains(item.getId()))
                .collect(Collectors.toList());

        if (cartItems.isEmpty()) {
            throw new ResourceNotFoundException("Cart is empty");
        }

        BillModel savedBill = billRepository.save(bill);

        List<LineItemModel> items = new ArrayList<>();
        Double totalPrice = 0.0;
        for (CartItemModel item : cartItems) {
            ProductModel product = item.getProductModel();
            if (productRepository.getQuantityProductInStock(product.getId()) < item.getQuantity()) {
                throw new ResourceNotFoundException("Not enough stock for product: " + product.getProductName());
            }
            LineItemModel lineItem = LineItemModel.builder()
                    .productModel(product)
                    .billModel(savedBill)
                    .quantity(item.getQuantity())
                    .build();
            double unitPrice = item.getProductModel().getUnitPrice();
            if (item.getProductModel().getDiscountDetailModel() != null
                    && item.getProductModel().getDiscountDetailModel().getPercentage() != null) {
                double discountPercentage = item.getProductModel().getDiscountDetailModel().getPercentage();
                unitPrice = product.getUnitPrice() * ((100 - discountPercentage) / 100);
            }
            lineItem.setUnitPrice(unitPrice);
            Integer quantity = item.getQuantity();
            totalPrice += unitPrice * quantity;

            List<SerialProductModel> activeSerials = product.getListSerialOfProduct().stream()
                    .filter(SerialProductModel::isActive)
                    .limit(item.getQuantity())
                    .toList();

            if (activeSerials.size() < item.getQuantity()) {
                throw new ResourceNotFoundException(
                        "Not enough active serials for product: " + product.getProductName());
            }

            for (SerialProductModel serial : activeSerials) {
                serial.setActive(false);
                serial.setLineItemModel(lineItem);
                serialProductRepository.save(serial);
            }
            lineItemRepository.save(lineItem);
            items.add(lineItem);
        }

        savedBill.setTotalPrice(totalPrice);
        if (coupon != null) {
            CouponDecorator couponDecorator = new CouponDecorator(savedBill, coupon);
            savedBill = couponDecorator.getBillModel();
            coupon.setActive(false);
            couponRepository.save(coupon);
        }
        savedBill.setItems(items);
        Double totalPriceWithShipping = totalPrice + request.getShippingFee();
        savedBill = billRepository.save(savedBill);

        for (CartItemModel item : cartItems) {
            cart.getCartItems().remove(item);
            cartItemRepository.delete(item);
        }
        cartRepository.save(cart);
        if (request.getPaymentMethod() == EPaymentMethod.ONLINE_BANKING) {
            String returnUrl = "https://nayami-shop-fe.vercel.app/checkout";
            String cancelUrl = "https://nayami-shop-fe.vercel.app/checkout";

            String paymentUrl = ((OnlineBankingPaymentStrategy) paymentStrategy)
                    .createCheckout(totalPriceWithShipping.intValue(), savedBill.getId(), returnUrl, cancelUrl);
            return paymentUrl;
        }

        return billMapper.toResponseDTO(savedBill);
    }

    @Transactional
    public List<HistoryOrderDTO> getBillHistory(String email) {
        if (email == null) {
            throw new ResourceNotFoundException("Vui lòng đăng nhập ");
        }
        UserModel customer = userRepository.getUserByEmail(email);
        if (customer == null) {
            throw new ResourceNotFoundException("User not found");
        }
        List<BillModel> billPage = billRepository.findByCustomerModelOrderByCreatedAtDesc(customer);
        List<HistoryOrderDTO> dtos = billMapper.toDTOList(billPage);

        return dtos;
    }

    @Override
    public List<BillDTO> getAllBills() {
        List<BillModel> billModelList = billRepository.findAllByOrderByCreatedAtDesc();
        return billModelList.stream().map(billMapper::toBillDTOFull).collect(Collectors.toList());
    }

    @Transactional
    public String cancelBill(String email, Long billId) {
        try {
            if (email == null) {
                throw new ResourceNotFoundException("Vui lòng đăng nhập");
            }
            UserModel customer = userRepository.getUserByEmail(email);
            if (customer == null) {
                throw new ResourceNotFoundException("User not found");
            }
            BillModel bill = billRepository.findByIdAndCustomerModel(billId, customer);
            if (bill == null) {
                throw new ResourceNotFoundException("Bill not found with id: " + billId);

            }
            if (bill.getStatus() == EBillStatus.CANCELLED) {
                throw new ResourceNotFoundException("Bill is already cancelled");
            }
            bill.setStatus(EBillStatus.CANCELLED);
            billRepository.save(bill);
            return "Đơn hàng hủy thành công";
        } catch (Exception e) {
            throw new CustomException("Lỗi hủy đơn hàng");
        }
    }

    @Override
    public void updateBill(String email, Long billId, String status) throws IOException {
        UserModel customer = userRepository.getUserByEmail(email);
        BillModel bill = billRepository.findByIdAndCustomerModel(billId, customer);
        if (bill == null) {
            throw new ResourceNotFoundException("Bill not found with id: " + billId);
        }
        EBillStatus billStatus = EBillStatus.valueOf(status);
        String subject = "";
        switch (billStatus) {
            case CONFIRMED:
                subject = "Đơn hàng của bạn đã được xác nhận";
                break;
            case SHIPPED:
                subject = "Đơn hàng của bạn đã được giao";
                bill.getPaymentModel().setPaymentStatus(EPaymentStatus.COMPLETED);
                break;
            case GUARANTEE:
                subject = "Đơn hàng của bạn đang được bảo hành";
                break;
            case SHIPPING:
                subject = "Đơn hàng của bạn đang được giao";
                break;
            case COMPLETED:
                subject = "Đơn hàng của bạn đã được hoàn thành";
                break;
            case CANCELLED:
                subject = "Đơn hàng của bạn đã bị hủy";
                break;
            default:
                throw new ResourceNotFoundException("Unknown bill status: " + billStatus);
        }
        emailService.sendEmailOrder(customer.getEmail(), subject, billMapper.toBillDetailDTO(bill));
        bill.setStatus(billStatus);
        billRepository.save(bill);
    }

    @Transactional
    public void confirmBill(String email, Long billCode) {

        if (email == null) {
            throw new ResourceNotFoundException("Vui lòng đăng nhập");
        }
        UserModel customer = userRepository.getUserByEmail(email);
        if (customer == null) {
            throw new ResourceNotFoundException("User not found");
        }

        PayOS payOS = new PayOS(clientId, apiKey, checksumKey);

        try {
            PaymentLinkData paymentLinkData = payOS.getPaymentLinkInformation(billCode);
            if (paymentLinkData.getStatus().equals("PAID")) {
                BillModel bill = billRepository.findByOrderNumberAndCustomerModel(billCode, customer);
                if (bill == null) {
                    throw new ResourceNotFoundException("Bill not found with id: " + billCode);

                }
                if (bill.getPaymentModel().getPaymentMethod() != EPaymentMethod.ONLINE_BANKING) {
                    throw new ResourceNotFoundException("Bill not found with id: " + billCode);
                }

                PaymentModel paymentModel = bill.getPaymentModel();
                paymentModel.setPaymentStatus(EPaymentStatus.COMPLETED);
                paymentRepository.save(paymentModel);

                billRepository.save(bill);
            }
        } catch (Exception e) {
            log.error("Error getting payment link information: {}", e.getMessage());
        }

    }

    @Override
    public Long countBillsByStatus(EBillStatus status) {
        return billRepository.countBillsByStatus(status);
    }

    @Override
    public Double totalRevenue(EBillStatus status) {
        return billRepository.totalRevenue(status);
    }

    @Override
    public Double totalProfit(EBillStatus status) {
        return billRepository.totalProfit(status);
    }

    @Scheduled(cron = "0 */10 * * * *")
    @Transactional
    public void cancelUnpaidOrders() {
        log.info("Running scheduled task to cancel unpaid orders");

        LocalDateTime cutoffTime = LocalDateTime.now().minus(24, ChronoUnit.HOURS);

        List<BillModel> unpaidBills = billRepository
                .findByPaymentModel_PaymentStatusAndCreatedAtBefore(EPaymentStatus.PENDING, cutoffTime);

        if (!unpaidBills.isEmpty()) {
            log.info("Found {} unpaid bills to cancel", unpaidBills.size());

            for (BillModel bill : unpaidBills) {
                bill.getPaymentModel().setPaymentStatus(EPaymentStatus.CANCELLED);
                bill.setStatus(EBillStatus.CANCELLED);
                log.info("Cancelling unpaid bill ID: {}", bill.getId());
            }

            billRepository.saveAll(unpaidBills);
            log.info("Successfully cancelled {} unpaid bills", unpaidBills.size());
        } else {
            log.info("No unpaid bills to cancel");
        }
    }

    @Scheduled(cron = "0 */10 * * * *")
    @Transactional
    public void completeDeliveredOrders() {
        log.info("Running scheduled task to auto-complete delivered orders after 7 days");

        LocalDateTime cutoffTime = LocalDateTime.now().minus(7, ChronoUnit.DAYS);

        List<BillModel> deliveredBills = billRepository.findByStatusAndUpdatedAtBefore(EBillStatus.SHIPPED, cutoffTime);

        if (!deliveredBills.isEmpty()) {
            log.info("Found {} delivered bills to auto-complete", deliveredBills.size());

            for (BillModel bill : deliveredBills) {
                bill.setStatus(EBillStatus.COMPLETED);
                log.info("Auto-completing bill ID: {} after 7 days in delivered status", bill.getId());
            }

            billRepository.saveAll(deliveredBills);
            log.info("Successfully auto-completed {} bills", deliveredBills.size());
        } else {
            log.info("No delivered bills to auto-complete");
        }
    }

    @Override
    public BillDetailDTO getBillByID(Long id) {
        return billMapper.toBillDetailDTO(billRepository.findById(id).orElse(null));
    }

    @Transactional
    public String RequestGuarantee(String email, Long billId) {
        if (email == null) {
            throw new ResourceNotFoundException("Vui lòng đăng nhập");
        }
        UserModel customer = userRepository.getUserByEmail(email);
        if (customer == null) {
            throw new ResourceNotFoundException("User not found");
        }
        BillModel bill = billRepository.findByIdAndCustomerModel(billId, customer);
        if (bill == null) {
            throw new ResourceNotFoundException("Bill not found with id: " + billId);

        }
        if (bill.getStatus() != EBillStatus.SHIPPED) {
            throw new ResourceNotFoundException("Bill is not completed yet");
        }
        bill.setStatus(EBillStatus.GUARANTEE);
        billRepository.save(bill);
        return "Đơn hàng đã được yêu cầu bảo hành";
    }

    @Override
    public List<ProductBestSellingDTO> getProductBestSellingByTime(LocalDate startDate, LocalDate endDate,
                                                                   EBillStatus status) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        List<ProductBestSellingDTO> data = new ArrayList<>();
        List<Object[]> listProduct = billRepository.topSeller(startDateTime, endDateTime, EBillStatus.COMPLETED);
        for (int i = 0; i < Math.min(5, listProduct.size()); i++) {
            Object[] productInfo = listProduct.get(i);
            if (productInfo != null && productInfo.length > 0) {
                ProductModel productModel = (ProductModel) productInfo[0];
                data.add(ProductBestSellingDTO.builder()
                        .id(productModel.getId())
                        .url(productModel.getListImage().get(0).getUrl())
                        .name(productModel.getProductName())
                        .unitPrice(productModel.getUnitPrice())
                        .quantity(productRepository.getQuantityProductInStock(productModel.getId()))
                        .quantitySold(((Long) productInfo[1]).intValue())
                        .build());
            }
        }
        return data;
    }

    @Override
    public DashBoardResponseDTO getRevenueOrProfitByTime(LocalDate startDate, LocalDate endDate, EBillStatus status,
                                                         int a) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        List<BillModel> listOrder = billRepository.revenueByTime(startDateTime, endDateTime, status);
        Map<String, Double> totalRevenueByDate = new ArrayMap<>();
        long numberDays = ChronoUnit.DAYS.between(startDate, endDate);
        for (BillModel orderModel : listOrder) {
            String orderDateStr;
            LocalDate orderDate = orderModel.getCreatedAt()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            if (numberDays <= 1) {
                orderDateStr = orderModel.getCreatedAt()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            } else if (numberDays <= 30) {
                orderDateStr = orderDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            } else if (numberDays <= 92) {
                int week = orderDate.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
                orderDateStr = "Week " + week + ", " + orderDate.getYear();
            } else if (numberDays <= 365 * 2) {
                orderDateStr = orderDate.format(DateTimeFormatter.ofPattern("MM-yyyy"));
            } else {
                orderDateStr = orderDate.format(DateTimeFormatter.ofPattern("yyyy"));
            }
            double totalAmount = orderModel.getTotalPrice();
            if (a == 1) {
                for (LineItemModel item : orderModel.getItems()) {
                    totalAmount -= item.getQuantity() * item.getProductModel().getOriginalPrice();
                }
            }
            totalRevenueByDate.put(orderDateStr, totalRevenueByDate.getOrDefault(orderDateStr, 0.0) + totalAmount);
        }
        List<String> time = new ArrayList<>(totalRevenueByDate.keySet());
        List<String> data = totalRevenueByDate.values().stream()
                .map(String::valueOf)
                .collect(Collectors.toList());

        return DashBoardResponseDTO.builder()
                .time(time)
                .data(data)
                .build();
    }

    public String handlePayment(String email, Long id) {
        BillModel bill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + id));
        if (bill.getCustomerModel().getEmail() == null || !bill.getCustomerModel().getEmail().equals(email)) {
            throw new ResourceNotFoundException("Bill not found with id: " + id);
        }

        if (bill.getPaymentModel().getPaymentMethod() == EPaymentMethod.ONLINE_BANKING) {
            String returnUrl = "https://nayami-shop-fe.vercel.app/checkout";
            String cancelUrl = "https://nayami-shop-fe.vercel.app/checkout";

            String paymentUrl = ((OnlineBankingPaymentStrategy) paymentStrategyFactory
                    .getStrategy(EPaymentMethod.ONLINE_BANKING.name()))
                    .createCheckout(bill.getTotalPrice().intValue(), bill.getId(), returnUrl, cancelUrl);

            return paymentUrl;
        }
        return null;
    }
}
