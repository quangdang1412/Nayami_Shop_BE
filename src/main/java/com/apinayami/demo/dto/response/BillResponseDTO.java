package com.apinayami.demo.dto.response;

import com.apinayami.demo.dto.request.LineItemDTO;
import com.apinayami.demo.dto.request.UserDTO;
import com.apinayami.demo.model.CouponModel;
import com.apinayami.demo.model.PaymentModel;
import com.apinayami.demo.model.ShippingModel;
import com.apinayami.demo.util.Enum.EPaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillResponseDTO {
    private Long id;
    private Double totalPrice;
    private Double discount;
    private EPaymentMethod paymentMethod;
    private String paymentUrl;
    private UserDTO customer;
    private ShippingModel shipping;
    private PaymentModel payment;
    private CouponModel coupon;
    private List<LineItemDTO> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}