package com.apinayami.demo.model;

import com.apinayami.demo.model.Product.BaseProduct;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class CommentModel extends AbstractEntity<Long> {
    private String description;
    private Integer rating;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private BaseProduct productModel;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerModel customerModel;

}
