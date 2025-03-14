package com.apinayami.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CartItemModel extends AbstractEntity<Long> {
    private Integer quantity;
    private Double unitPrice;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductModel productModel;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private UserModel customerModel;
}