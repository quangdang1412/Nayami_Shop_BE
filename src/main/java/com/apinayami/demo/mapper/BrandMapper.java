package com.apinayami.demo.mapper;

import com.apinayami.demo.dto.request.BrandDTO;
import com.apinayami.demo.model.BrandModel;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {

    public BrandDTO toDetailDto(BrandModel brand) {
        if (brand == null) {
            return null;
        }

        return BrandDTO.builder()
                .id(brand.getId())
                .name(brand.getBrandName())
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
        brand.setActive(dto.isActive());
        return brand;
    }

    public void updateEntityFromDto(BrandDTO dto, BrandModel brand) {
        if (dto == null || brand == null) {
            return;
        }

        brand.setBrandName(dto.getName());
    }
}