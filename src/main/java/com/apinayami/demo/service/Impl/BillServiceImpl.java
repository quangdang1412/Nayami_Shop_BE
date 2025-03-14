package com.apinayami.demo.service.Impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.sound.sampled.Line;

import org.springframework.stereotype.Service;

import com.apinayami.demo.dto.request.BillRequestDTO;
import com.apinayami.demo.dto.request.CartItemDTO;
import com.apinayami.demo.dto.response.BillResponseDTO;
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
import com.apinayami.demo.util.Enum.EPaymentStatus;

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
    
    @Transactional
    public BillResponseDTO createBill(String email,BillRequestDTO request) {
       
        UserModel customer = userRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        if (!email.equals(customer.getEmail())) {
                return null;
        }
        ShippingModel shipping = shippingRepository.findById(request.getShippingId())
                .orElseThrow(() -> new RuntimeException("Shipping not found"));

        CouponModel coupon = request.getCouponId() != null ? couponRepository.findById(request.getCouponId()).orElse(null) : null;

        PaymentModel payment = PaymentModel.builder()
                .paymentMethod(request.getPaymentMethod())
                .currency(EPaymentCurrency.VND)
                .paymentStatus(EPaymentStatus.PENDING) 
                .build();
        payment = paymentRepository.save(payment);

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
        Double totalPrice=0.0;
        for (CartItemModel item : cartItem) {
            totalPrice += item.getUnitPrice() * item.getQuantity();
        }
        List<LineItemModel> items = cartItem.stream()
        .map((CartItemModel item) -> { 
            ProductModel product = item.getProductModel();
            if (product == null) {
                throw new RuntimeException("ProductModel is null for CartItem: " + item.getId());
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
        return billMapper.toResponseDTO(savedBill);
    }
}
