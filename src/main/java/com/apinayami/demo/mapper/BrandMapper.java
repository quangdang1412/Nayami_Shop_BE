package com.apinayami.demo.mapper;

import com.apinayami.demo.dto.request.BrandDTO;
import com.apinayami.demo.model.BrandModel;
import com.apinayami.demo.model.ProductModel;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BrandMapper {

    public BrandDTO toDetailDto(BrandModel brand) {
        if (brand == null) {
            return null;
        }

        List<String> productNames = brand.getListProduct() != null
                ? brand.getListProduct().stream()
                        .map(ProductModel::getProductName)
                        .collect(Collectors.toList())
                : List.of();

        return BrandDTO.builder()
                .id(brand.getId())
                .name(brand.getBrandName())
                .productNames(productNames)
                .build();
    }

    public BrandModel toEntity(BrandDTO dto) {
        if (dto == null) {
            return null;
        }

        BrandModel brand = new BrandModel();
        if (dto.getId() != null) {
            brand.setId(dto.getId());
        }
        brand.setBrandName(dto.getName());
        return brand;
    }

    public void updateEntityFromDto(BrandDTO dto, BrandModel brand) {
        if (dto == null || brand == null) {
            return;
        }

        brand.setBrandName(dto.getName());
    }
}