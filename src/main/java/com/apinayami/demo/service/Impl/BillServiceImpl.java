package com.apinayami.demo.service.Impl;

import com.apinayami.demo.dto.request.BillRequestDTO;
import com.apinayami.demo.dto.request.CartPaymentDTO;
import com.apinayami.demo.dto.response.BillResponseDTO;
import com.apinayami.demo.dto.response.HistoryOrderDTO;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.exception.ResourceNotFoundException;
import com.apinayami.demo.mapper.AddressMapper;
import com.apinayami.demo.mapper.BillMapper;
import com.apinayami.demo.mapper.CartItemMapper;
import com.apinayami.demo.model.*;
import com.apinayami.demo.repository.*;
import com.apinayami.demo.service.IBillService;
import com.apinayami.demo.util.Enum.EBillStatus;
import com.apinayami.demo.util.Enum.EPaymentMethod;
import com.apinayami.demo.util.Enum.EPaymentStatus;
import com.apinayami.demo.util.Strategy.OnlineBankingPaymentStrategy;
import com.apinayami.demo.util.Strategy.PaymentStrategy;
import com.apinayami.demo.util.Strategy.PaymentStrategyFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
        CouponModel coupon = request.getCouponId() != null
                ? couponRepository.findById(request.getCouponId()).orElse(null)
                : null;

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
        Double totalPrice = 0.0;
        List<LineItemModel> items = new ArrayList<>();
        List<CartItemModel> cartItem = cartItemRepository.findByCustomerModel(customer);
        if (cartItem.isEmpty()) {
            throw new ResourceNotFoundException("Cart is empty");
        } else {
            for (CartItemModel item : cartItem) {
                ProductModel productModel = productRepository.findById(item.getProductModel().getId())
                        .orElseThrow(() -> new CustomException("Sản phẩm không tồn tại"));
                if (productModel.getQuantity() < item.getQuantity()) {
                    throw new CustomException("Sản phẩm " + productModel.getProductName() + " không đủ số lượng");
                }
                if (productModel.getDiscountDetailModel() != null) {
                    totalPrice += productModel.getUnitPrice() * (100 - productModel.getDiscountDetailModel().getPercentage()) / 100 * item.getQuantity();
                } else {
                    totalPrice += productModel.getUnitPrice() * item.getQuantity();
                }
                productModel.setQuantity(productModel.getQuantity() - item.getQuantity());
                productRepository.save(productModel);
                items.add(LineItemModel.builder()
                        .productModel(productModel)
                        .billModel(bill)
                        .quantity(item.getQuantity())
                        .unitPrice(productModel.getUnitPrice())
                        .build());
            }

        }
        bill.setItems(items);
        bill.setTotalPrice(totalPrice);
        BillModel savedBill = billRepository.save(bill);
        cartItemRepository.deleteByCustomerModel(customer);
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

    @Transactional
    public void cancelBill(String email, Long billId) {
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
}
