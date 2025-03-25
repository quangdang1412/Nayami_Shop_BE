package com.apinayami.demo.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"listProduct"})
public class BrandModel extends AbstractEntity<Long> {
    private String brandName;

    // reference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "brandModel", cascade = CascadeType.ALL)
    Set<ProductModel> listProduct;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean active;
}