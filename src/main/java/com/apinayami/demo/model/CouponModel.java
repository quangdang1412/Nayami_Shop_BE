package com.apinayami.demo.model;

import com.apinayami.demo.util.Enum.ETypeCoupon;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CouponModel extends AbstractEntity<Long> {
    @ManyToOne()
    @JoinColumn(name = "customer_id")
    private UserModel customerModel;
    private String content;
    private Double value;
    @Enumerated(EnumType.STRING)
    private ETypeCoupon type;
    private Double constraintMoney;
    private boolean active;
}
