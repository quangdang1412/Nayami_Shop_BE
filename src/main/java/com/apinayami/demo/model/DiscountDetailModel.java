package com.apinayami.demo.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"listProduct", "discountCampaignModel"})
public class DiscountDetailModel extends AbstractEntity<Long> {

    private Double percentage;

    //reference
    @ManyToOne()
    @JoinColumn(name = "discount_campaign_id")
    @ToString.Exclude
    private DiscountCampaignModel discountCampaignModel;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "discountDetailModel", cascade = CascadeType.ALL)
    List<ProductModel> listProduct;
}
