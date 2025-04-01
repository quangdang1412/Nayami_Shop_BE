package com.apinayami.demo.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"listProduct", "listConfiguration"})
public class CategoryModel extends AbstractEntity<Long> {
    private String categoryName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "categoryModel", cascade = CascadeType.ALL)
    Set<ProductModel> listProduct;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "categoryModel", cascade = CascadeType.ALL)
    List<ConfigurationModel> listConfiguration;

}
