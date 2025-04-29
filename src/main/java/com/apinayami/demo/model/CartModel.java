package com.apinayami.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CartModel extends AbstractEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private UserModel customerModel;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cartModel", cascade = CascadeType.ALL)
    private List<CartItemModel> cartItems = new ArrayList<>();
}