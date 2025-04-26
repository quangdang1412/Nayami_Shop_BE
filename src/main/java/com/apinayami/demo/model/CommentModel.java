package com.apinayami.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
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
    @Column(columnDefinition = "BOOLEAN default true")
    private boolean active = true;

    //reference
    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductModel productModel;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private UserModel customerModel;
}
