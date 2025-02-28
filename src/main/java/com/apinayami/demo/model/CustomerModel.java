package com.apinayami.demo.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CustomerModel extends UserModel {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customerModel", cascade = CascadeType.ALL)
    private Set<AddressModel> listAddress;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customerModel", cascade = CascadeType.ALL)
    private Set<CouponModel> listCoupon;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customerModel", cascade = CascadeType.ALL)
    private Set<CartItemModel> listCartItem;
}
