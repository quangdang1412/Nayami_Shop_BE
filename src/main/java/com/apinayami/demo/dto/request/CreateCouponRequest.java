package com.apinayami.demo.dto.request;

import java.time.LocalDate;

import com.apinayami.demo.util.Enum.ETypeCoupon;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCouponRequest {
    private Long customerId;

    private String content;

    private Double value;

    private ETypeCoupon type;

    private Double constraintMoney;

    @NotNull(message = "Start date cannot be null")
    private LocalDate startDate;
    
    @NotNull(message = "End date cannot be null")
    private LocalDate endDate;

}