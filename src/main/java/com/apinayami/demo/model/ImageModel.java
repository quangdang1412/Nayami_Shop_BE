package com.apinayami.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class ImageModel extends AbstractEntity<Long> {
    private String url;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductModel productModel;
    @ManyToOne
    @JoinColumn(name = "promotion_id")
    @JsonBackReference
    private PromotionModel promotionModel;
}
