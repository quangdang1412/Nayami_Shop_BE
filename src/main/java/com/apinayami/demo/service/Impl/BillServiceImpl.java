package com.apinayami.demo.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.sound.sampled.Line;

import com.apinayami.demo.dto.response.*;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.apinayami.demo.dto.request.BillRequestDTO;
import com.apinayami.demo.dto.request.CartPaymentDTO;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.exception.ResourceNotFoundException;
import com.apinayami.demo.mapper.AddressMapper;
import com.apinayami.demo.mapper.BillMapper;
import com.apinayami.demo.mapper.CartItemMapper;
import com.apinayami.demo.model.AddressModel;
import com.apinayami.demo.model.BillModel;
import com.apinayami.demo.model.CartItemModel;
import com.apinayami.demo.model.CouponModel;
import com.apinayami.demo.model.LineItemModel;
import com.apinayami.demo.model.PaymentModel;
import com.apinayami.demo.model.ProductModel;
import com.apinayami.demo.model.ShippingModel;
import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.repository.IAddressRepository;
import com.apinayami.demo.repository.IBillRepository;
import com.apinayami.demo.repository.ICartItemRepository;
import com.apinayami.demo.repository.ICouponRepository;
import com.apinayami.demo.repository.ILineItemRepository;
import com.apinayami.demo.repository.IPaymentRepository;
import com.apinayami.demo.repository.IProductRepository;
import com.apinayami.demo.repository.IShippingRepository;
import com.apinayami.demo.repository.IUserRepository;
import com.apinayami.demo.service.IBillService;
import com.apinayami.demo.util.Enum.EBillStatus;
import com.apinayami.demo.util.Enum.EPaymentMethod;
import com.apinayami.demo.util.Enum.EPaymentStatus;
import com.apinayami.demo.util.Enum.ETypeCoupon;
import com.apinayami.demo.util.Strategy.OnlineBankingPaymentStrategy;
import com.apinayami.demo.util.Strategy.PaymentStrategy;
import com.apinayami.demo.util.Strategy.PaymentStrategyFactory;
import com.google.api.client.util.ArrayMap;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    private final IPaymentRepository paymentRepository;
    private final ICartItemRepository cartItemRepository;
    private final IAddressRepository addressRepository;
    private final ILineItemRepository lineItemRepository;
    private final BillMapper billMapper;
    private final PaymentStrategyFactory paymentStrategyFactory;
    private final CartItemMapper cartItemMapper;
    private final AddressMapper addressMapper;

    @Transactional
    public BillResponseDTO getBill(String email, CartPaymentDTO billRequestDTO) {
        UserModel customer = userRepository.getUserByEmail(email);
        if (customer == null) {
            throw new ResourceNotFoundException("User is empty");
        }
        BillResponseDTO bill = new BillResponseDTO();
        List<CartItemModel> cartItems = billRequestDTO.getCartId().stream()
                .map(id -> cartItemRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("CartItem not found with id: " + id)))
                .collect(Collectors.toList());
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
        if(request.getCouponId() != null) {
            coupon = couponRepository.findByIdAndActiveTrue(request.getCouponId())
                    .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: " + request.getCouponId()));
            coupon.setActive(false);
            couponRepository.save(coupon);
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
        List<CartItemModel> cartItem = request.getCartId().stream()
            .map(id -> cartItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem not found with id: " + id)))
            .collect(Collectors.toList());

        if (cartItem.isEmpty()) {
            throw new ResourceNotFoundException("Cart is empty");
        }
        List<LineItemModel> items= new ArrayList<>();
        Double totalPrice = 0.0;
        for (CartItemModel item : cartItem) {
            double unitPrice = item.getProductModel().getUnitPrice();
            if(item.getProductModel().getDiscountDetailModel() != null && item.getProductModel().getDiscountDetailModel().getPercentage() != null) {
                double discountPercentage = item.getProductModel().getDiscountDetailModel().getPercentage();
                double discountAmountPerUnit = unitPrice * (discountPercentage / 100);
                unitPrice -= discountAmountPerUnit;  
            }
            Integer quantity = item.getQuantity();
            totalPrice += unitPrice * quantity;
            ProductModel product = item.getProductModel();
            if (product.getQuantity() < item.getQuantity()) {
                throw new ResourceNotFoundException("Not enough stock for product: " + product.getProductName());
            }
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepository.save(product);
            LineItemModel lineItem = LineItemModel.builder()
                    .productModel(product)
                    .billModel(bill)
                    .quantity(item.getQuantity())
                    .build();
            if(product.getDiscountDetailModel() != null && product.getDiscountDetailModel().getPercentage() != null) {
                lineItem.setUnitPrice(unitPrice-unitPrice*(product.getDiscountDetailModel().getPercentage()/100));
            }
            else {
                lineItem.setUnitPrice(unitPrice);
            }
            lineItemRepository.save(lineItem);
            items.add(lineItem);
           
            
        }
        if(coupon != null) {
            if (coupon.getConstraintMoney() != null && totalPrice < coupon.getConstraintMoney()) {
                throw new CustomException("Đơn hàng không đủ điều kiện sử dụng mã giảm giá này");
            }
            if (coupon.getValue() != null && coupon.getValue() > 0) {
                if (coupon.getType() == ETypeCoupon.PERCENT) {
                    totalPrice -= totalPrice * (coupon.getValue() / 100);
                } else if (coupon.getType() == ETypeCoupon.MONEY) {
                    totalPrice -= coupon.getValue();
                    
                }
            }
        }
        bill.setItems(items);
        bill.setTotalPrice(totalPrice);
        for (CartItemModel item : cartItem) {
            cartItemRepository.delete(item);
        }
        
        BillModel savedBill = billRepository.save(bill);
        if (request.getPaymentMethod() == EPaymentMethod.ONLINE_BANKING) {
            String returnUrl = "http://localhost:5173/checkout";
            String cancelUrl = "http://localhost:5173/checkout";

            String paymentUrl = ((OnlineBankingPaymentStrategy) paymentStrategy)
                    .createCheckout(totalPrice.intValue(), savedBill.getId(), returnUrl, cancelUrl);

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
        List<BillModel> billModelList = billRepository.findAll();
        return billModelList.stream().map(billMapper::toBillDTOFull).collect(Collectors.toList());
    }

    @Transactional
    public String cancelBill(String email, Long billId) {
        try{
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
        }
        catch (Exception e){
            throw new CustomException("Lỗi hủy đơn hàng");
        }
    }

    @Override
    public void updateBill(String email, Long billId, String status) {
        UserModel customer = userRepository.getUserByEmail(email);
        BillModel bill = billRepository.findByIdAndCustomerModel(billId, customer);
        for (EBillStatus billStatus : EBillStatus.values()) {
            if (billStatus.name().equalsIgnoreCase(status)) {
                bill.setStatus(billStatus);
                billRepository.save(bill);
                break;
            }
        }
    }

    @Transactional
    public void confirmBill(String email, Long billId) {
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
        if (bill.getPaymentModel().getPaymentMethod() != EPaymentMethod.ONLINE_BANKING) {
            throw new ResourceNotFoundException("Bill not found with id: " + billId);
        }
        bill.setPaymentModel(PaymentModel.builder().paymentStatus(EPaymentStatus.COMPLETED).build());
        billRepository.save(bill);
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

    @Override
    public DashBoardResponseDTO getRevenueByTime(LocalDate startDate, LocalDate endDate, EBillStatus status) {
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

    @Override
    public DashBoardResponseDTO getProfitByTime(LocalDate startDate, LocalDate endDate, EBillStatus status) {
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
            for (LineItemModel item : orderModel.getItems()) {
                totalAmount -= item.getQuantity() * item.getProductModel().getOriginalPrice();
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
    @Scheduled(cron = "0 */10 * * * *")
    @Transactional
    public void cancelUnpaidOrders() {
        log.info("Running scheduled task to cancel unpaid orders");
        
        LocalDateTime cutoffTime = LocalDateTime.now().minus(24, ChronoUnit.HOURS);
        
        List<BillModel> unpaidBills = billRepository.findByPaymentModel_PaymentStatusAndCreatedAtBefore(EPaymentStatus.PENDING, cutoffTime);
            
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

}
