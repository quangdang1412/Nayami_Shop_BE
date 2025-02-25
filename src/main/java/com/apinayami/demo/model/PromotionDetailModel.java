package com.apinayami.demo.model;

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
public class PromotionDetailModel extends AbstractEntity<Long> {
    @OneToOne
    @JoinColumn(name = "image_id")
    private ImageModel imageModel;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private PromotionModel promotionModel;
    private String position;
}
