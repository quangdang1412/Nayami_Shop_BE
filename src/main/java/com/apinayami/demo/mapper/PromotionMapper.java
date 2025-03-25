package com.apinayami.demo.mapper;

import com.apinayami.demo.dto.request.CategoryDTO;
import com.apinayami.demo.dto.request.PromotionDTO;
import com.apinayami.demo.model.CategoryModel;
import com.apinayami.demo.model.PromotionModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PromotionMapper {
    PromotionMapper INSTANCE = Mappers.getMapper(PromotionMapper.class);

    PromotionModel toPromotionModel(PromotionDTO dto);

    PromotionDTO toPromotionDTO(PromotionModel model);
}
