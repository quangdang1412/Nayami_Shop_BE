package com.apinayami.demo.model;

import com.apinayami.demo.model.Product.BaseProduct;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
public class ImageModel extends AbstractEntity<Long> {
    @ManyToOne
    @JoinColumn(name = "product_id")
    private BaseProduct productModel;
    @OneToOne
    @JoinColumn(name = "promotion_detail_id")
    private PromotionDetailModel promotionDetailModel;
    private String url;

}
