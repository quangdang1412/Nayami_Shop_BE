package com.apinayami.demo.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"listDiscountDetail"})
public class DiscountCampaignModel extends AbstractEntity<Long> {
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private boolean active;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "discountCampaignModel", cascade = CascadeType.ALL)
    List<DiscountDetailModel> listDiscountDetail;
}
