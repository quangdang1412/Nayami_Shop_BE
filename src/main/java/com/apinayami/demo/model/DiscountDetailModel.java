package com.apinayami.demo.model;

import jakarta.persistence.*;
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
public class DiscountDetailModel extends AbstractEntity<Long> {

    @ManyToOne()
    @JoinColumn(name = "discount_campaign_id")
    private DiscountCampaignModel discountCampaignModel;
    private Double percentage;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "discountDetailModel", cascade = CascadeType.ALL)
    Set<ProductModel> listProduct;
}
