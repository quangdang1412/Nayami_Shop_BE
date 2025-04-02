package com.apinayami.demo.model;

import com.apinayami.demo.util.Enum.EBillStatus;
import com.apinayami.demo.util.Enum.EPaymentMethod;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BillModel extends AbstractEntity<Long> {

    private Double totalPrice;
    private Double discount;
    @Enumerated(EnumType.STRING)
    private EPaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    private EBillStatus status;

    private String orderNumber;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private UserModel customerModel;

    @OneToOne
    @JoinColumn(name = "shipping_id")
    private ShippingModel shippingModel;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private PaymentModel paymentModel;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private CouponModel couponModel;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "billModel", cascade = CascadeType.ALL)
    private List<LineItemModel> items;
}
