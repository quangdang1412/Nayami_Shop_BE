package com.apinayami.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Objects;
import java.util.Set;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = { "listProduct", "configuration" })
public class CategoryModel extends AbstractEntity<Long> {
    private String categoryName;

    // reference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "categoryModel", cascade = CascadeType.ALL)
    Set<ProductModel> listProduct;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "categoryModel", cascade = CascadeType.ALL)
    ConfigurationModel configuration;

}
