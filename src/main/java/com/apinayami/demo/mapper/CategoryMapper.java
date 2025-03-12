package com.apinayami.demo.mapper;

import com.apinayami.demo.dto.request.CategoryDTO;
import com.apinayami.demo.model.CategoryModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryModel toCategoryModel(CategoryDTO dto);

    CategoryDTO toCategoryDTO(CategoryModel model);
}
