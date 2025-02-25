package com.apinayami.demo.model;

import com.apinayami.demo.util.Enum.ETypeCoupon;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CouponModel extends AbstractEntity<Long> {
    @ManyToOne()
    @JoinColumn(name = "customer_id")
    private CustomerModel customerModel;
    private String content;
    private Double value;
    @Enumerated(EnumType.STRING)
    private ETypeCoupon type;
    private Double constraintMoney;
    private boolean active;
}
