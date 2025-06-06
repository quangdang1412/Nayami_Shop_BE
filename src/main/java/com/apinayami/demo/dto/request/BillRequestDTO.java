package com.apinayami.demo.dto.request;

import java.util.List;

import com.apinayami.demo.util.Enum.EPaymentMethod;

import lombok.Data;

@Data
public class BillRequestDTO {
    private Double discount;
    private EPaymentMethod paymentMethod;
    private Long AddressId;
    private double ShippingFee;
    private String couponId;
    List<Long> cartId;
}