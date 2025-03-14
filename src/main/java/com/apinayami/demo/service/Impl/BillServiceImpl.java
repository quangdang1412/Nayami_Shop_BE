package com.apinayami.demo.service.Impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.sound.sampled.Line;

import org.springframework.stereotype.Service;

import com.apinayami.demo.dto.request.BillRequestDTO;
import com.apinayami.demo.dto.request.CartItemDTO;
import com.apinayami.demo.dto.response.BillResponseDTO;
import com.apinayami.demo.exception.ResourceNotFoundException;
import com.apinayami.demo.mapper.BillMapper;
import com.apinayami.demo.model.BillModel;
import com.apinayami.demo.model.CartItemModel;
import com.apinayami.demo.model.CouponModel;
import com.apinayami.demo.model.LineItemModel;
import com.apinayami.demo.model.PaymentModel;
import com.apinayami.demo.model.ProductModel;
import com.apinayami.demo.model.ShippingModel;
import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.repository.IBillRepository;
import com.apinayami.demo.repository.ICartItemRepository;
import com.apinayami.demo.repository.ICouponRepository;
import com.apinayami.demo.repository.IPaymentRepository;
import com.apinayami.demo.repository.IProductRepository;
import com.apinayami.demo.repository.IShippingRepository;
import com.apinayami.demo.repository.IUserRepository;
import com.apinayami.demo.service.IBillService;
import com.apinayami.demo.util.Enum.EPaymentCurrency;
import com.apinayami.demo.util.Enum.EPaymentMethod;
import com.apinayami.demo.util.Enum.EPaymentStatus;
import com.apinayami.demo.util.Strategy.OnlineBankingPaymentStrategy;
import com.apinayami.demo.util.Strategy.PaymentStrategy;
import com.apinayami.demo.util.Strategy.PaymentStrategyFactory;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class BillServiceImpl implements IBillService {
    private final IBillRepository billRepository;
    private final IUserRepository userRepository;
    private final IShippingRepository shippingRepository;
    private final ICouponRepository couponRepository;
    private final IPaymentRepository paymentRepository;
    private final ICartItemRepository  cartItemRepository;
    private final BillMapper billMapper;
    private final PaymentStrategyFactory  paymentStrategyFactory;


    
    @Transactional
    public Object createBill(String email,BillRequestDTO request) {
       
        UserModel customer = userRepository.getUserByEmail(email);
        if (customer == null) {
                throw new ResourceNotFoundException("User is empty");
        }
        ShippingModel shipping = shippingRepository.findById(request.getShippingId())
                .orElseThrow(() -> new ResourceNotFoundException("Shipping not found"));

        CouponModel coupon = request.getCouponId() != null ? couponRepository.findById(request.getCouponId()).orElse(null) : null;

        PaymentStrategy paymentStrategy = paymentStrategyFactory.getStrategy(request.getPaymentMethod().name());
        PaymentModel payment = paymentRepository.save(paymentStrategy.processPayment(request));
        BillModel bill = BillModel.builder()
                .totalPrice(123241.1)
                .discount(request.getDiscount())
                .paymentMethod(request.getPaymentMethod())
                .customerModel(customer)
                .shippingModel(shipping)
                .couponModel(coupon)
                .paymentModel(payment)
                .build();
        List<CartItemModel> cartItem = cartItemRepository.findByCustomerModel(customer);
        if (cartItem.isEmpty()) {
            throw new ResourceNotFoundException("Cart is empty");
        }
        Double totalPrice=0.0;
        for (CartItemModel item : cartItem) {
            totalPrice += item.getUnitPrice() * item.getQuantity();
        }
        List<LineItemModel> items = cartItem.stream()
        .map((CartItemModel item) -> { 
            ProductModel product = item.getProductModel();
            if (product == null) {
                throw new ResourceNotFoundException("ProductModel is null for CartItem: " + item.getId());
            }
            return LineItemModel.builder()
                    .productModel(product)
                    .billModel(bill)
                    .quantity(item.getQuantity())
                    .unitPrice(product.getUnitPrice())
                    .build();
            
        })
        .collect(Collectors.toList());
        bill.setItems(items);
        cartItemRepository.deleteByCustomerModel(customer);
        bill.setTotalPrice(totalPrice);
        BillModel savedBill = billRepository.save(bill);
        if (request.getPaymentMethod() == EPaymentMethod.ONLINE_BANKING) {
                String returnUrl = "/api/bills";
                String cancelUrl = "/api/bills";
                
                String paymentUrl = ((OnlineBankingPaymentStrategy) paymentStrategy)
                        .createCheckout(totalPrice.intValue(), savedBill.getId().toString(), returnUrl, cancelUrl);

                return paymentUrl; 
        }
        return billMapper.toResponseDTO(savedBill);
    }
}
