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

import java.util.Set;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BrandModel extends AbstractEntity<Long> {
    private String brandName;

    // reference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "brandModel", cascade = CascadeType.ALL)
    Set<ProductModel> listProduct;

//    @Override
//    public boolean equals(Object o) {
//        if (this == o)
//            return true;
//        if (!(o instanceof BrandModel))
//            return false;
//        BrandModel that = (BrandModel) o;
//        return getId() != null && getId().equals(that.getId());
//    }
//
//    @Override
//    public int hashCode() {
//        return getClass().hashCode();
//    }
}