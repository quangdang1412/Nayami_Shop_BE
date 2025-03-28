package com.apinayami.demo.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class CartPaymentDTO {
    List<Long> cartId;
    private String couponId;
    private Double discount;
}
