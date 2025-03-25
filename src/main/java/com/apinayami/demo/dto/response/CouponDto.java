package com.apinayami.demo.dto.response;

import java.time.LocalDate;

import com.apinayami.demo.util.Enum.ETypeCoupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponDto {
    private String id;
    private Long customerId;
    private Double value;
    private String content;
    private ETypeCoupon type;
    private Double constraintMoney;
    private boolean active;
    private LocalDate endDate;
    private LocalDate startDate;

}