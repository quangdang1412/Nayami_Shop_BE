package com.apinayami.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CartItemModel extends AbstractEntity<Long>  {


    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductModel productModel;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private CartModel cartModel;
}