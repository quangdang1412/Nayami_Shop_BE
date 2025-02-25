package com.apinayami.demo.model;

import com.apinayami.demo.util.Enum.EPaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BillModel extends AbstractEntity<Long> {
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerModel customerModel;
    private EPaymentMethod paymentMethod;
    @OneToOne
    @JoinColumn(name = "shipping_id")
    private ShippingModel shippingModel;
    @OneToOne
    @JoinColumn(name = "payment_id")
    private PaymentModel paymentModel;
    private Double totalPrice;
    private Double discount;
    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private CouponModel couponModel;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "billModel", cascade = CascadeType.ALL)
    private List<LineItemModel> listLineItem;
}
