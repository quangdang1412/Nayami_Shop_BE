package com.apinayami.demo.dto.response;

import com.apinayami.demo.dto.request.UserDTO;
import com.apinayami.demo.util.Enum.EBillStatus;
import com.apinayami.demo.util.Enum.EPaymentMethod;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillDetailDTO implements Serializable {
    private LocalDateTime createdAt;
    private Double totalPrice;
    private Double discount;
    @Enumerated(EnumType.STRING)
    private EPaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    private EBillStatus status;
    private String orderNumber;
    private Long id;

    private UserDTO customer;
    private ShippingDTO shipping;
    private PaymentDTO payment;
    private CouponDto coupon;
    private List<LineItemReponseDTO> items;
}
