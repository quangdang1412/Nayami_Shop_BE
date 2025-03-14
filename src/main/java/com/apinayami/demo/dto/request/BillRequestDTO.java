package com.apinayami.demo.dto.request;


import java.util.List;

import com.apinayami.demo.util.Enum.EPaymentMethod;

import lombok.Data;

@Data
public class BillRequestDTO {
    private Double discount;
    private EPaymentMethod paymentMethod;
    private Long customerId;
    private Long shippingId;
    private Long couponId;
    private List<CartItemDTO> cartItem;
}