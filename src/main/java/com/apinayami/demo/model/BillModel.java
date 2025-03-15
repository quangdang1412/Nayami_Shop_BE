package com.apinayami.demo.model;

import com.apinayami.demo.util.Enum.EPaymentMethod;
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
    private EPaymentMethod paymentMethod;
    //reference
    @ManyToOne
    @JoinColumn(name = "customer_id")
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
