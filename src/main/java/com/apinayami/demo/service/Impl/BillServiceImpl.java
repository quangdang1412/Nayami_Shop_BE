package com.apinayami.demo.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.sound.sampled.Line;

import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.apinayami.demo.dto.request.BillRequestDTO;
import com.apinayami.demo.dto.request.CartItemDTO;
import com.apinayami.demo.dto.request.CartPayment;
import com.apinayami.demo.dto.response.BillResponseDTO;
import com.apinayami.demo.dto.response.HistoryOrderDTO;
import com.apinayami.demo.dto.response.ResponseData;
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
import com.apinayami.demo.repository.IPaymentRepository;
import com.apinayami.demo.repository.IShippingRepository;
import com.apinayami.demo.repository.IUserRepository;
import com.apinayami.demo.service.IBillService;
import com.apinayami.demo.util.Enum.EBillStatus;
import com.apinayami.demo.util.Enum.EPaymentMethod;
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
    private final IAddressRepository addressRepository;
    private final BillMapper billMapper;
    private final PaymentStrategyFactory  paymentStrategyFactory;
    private final CartItemMapper cartItemMapper;
    private final AddressMapper addressMapper;

    @Transactional
    public BillResponseDTO getBill(String email,CartPayment billRequestDTO){
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
        bill.setDiscount(null);
        return bill;

    }

    
    @Transactional
    public Object createBill(String email,BillRequestDTO request) {
       
        UserModel customer = userRepository.getUserByEmail(email);
        if (customer == null) {
                throw new ResourceNotFoundException("User is empty");
        }
        
        AddressModel address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + request.getAddressId()));
        ShippingModel shipping = ShippingModel.builder()
                .shippingFee(request.getShippingFee())
                .addressModel(address)
                .build();
        shipping = shippingRepository.save(shipping);
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
                .status(EBillStatus.PENDING)
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
    @Transactional
    public Page<HistoryOrderDTO> getBillHistory(String email,Pageable pageable) {
         if (email == null) {
            throw new ResourceNotFoundException("Vui lòng đăng nhập ");
        } 
        UserModel customer = userRepository.getUserByEmail(email);
        if (customer == null) {
            throw new ResourceNotFoundException("User not found");
        }
        Page<BillModel> billPage = billRepository.findByCustomerModel(customer, pageable);
        List<HistoryOrderDTO> dtos = billMapper.toDTOList(billPage.getContent());

        return new PageImpl<>(dtos, pageable, billPage.getTotalElements());
    }
}

