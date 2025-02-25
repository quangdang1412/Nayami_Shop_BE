package com.apinayami.demo.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.Set;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DiscountCampaignModel extends AbstractEntity<Long> {
    private String description;
    private Date startDate;
    private Date endDate;
    private boolean active;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "discountCampaignModel", cascade = CascadeType.ALL)
    Set<DiscountDetailModel> listDiscountDetail;
}
