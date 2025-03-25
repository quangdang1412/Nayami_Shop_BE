package com.apinayami.demo.model;

import java.time.LocalDate;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
public class CouponModel {
    
    @Id
    @Column(name = "id")
    private String id;
    @ManyToOne()
    @JoinColumn(name = "customer_id")
    private UserModel customerModel;
    private String content;
    private Double value;
    @Enumerated(EnumType.STRING)
    private ETypeCoupon type;
    private Double constraintMoney;
    
    @Column(columnDefinition = "TINYINT(1)")
    private boolean active;

    private LocalDate startDate;
    private LocalDate endDate;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    protected Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    protected Date updatedAt;
}
