package com.apinayami.demo.model;

import com.apinayami.demo.model.Product.BaseProduct;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CategoryModel extends AbstractEntity<Long> {
    private String categoryName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "categoryModel", cascade = CascadeType.ALL)
    @Nullable // test
    private List<BaseProduct> products;
}
