package com.apinayami.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@EqualsAndHashCode(callSuper = true)
public class PromotionModel extends AbstractEntity<Long> {
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private boolean displayStatus;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "promotionModel", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ImageModel> promotionImages;
}
