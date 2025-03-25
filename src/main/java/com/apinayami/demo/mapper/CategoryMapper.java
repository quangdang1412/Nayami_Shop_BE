package com.apinayami.demo.mapper;

import com.apinayami.demo.dto.request.CategoryDTO;
import com.apinayami.demo.model.CategoryModel;
import org.springframework.stereotype.Component;

//@Mapper(componentModel = "spring")
//public interface CategoryMapper {
//    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
//
//    CategoryModel toCategoryModel(CategoryDTO dto);
//
//    CategoryDTO toCategoryDTO(CategoryModel model);
//}
@Component
public class CategoryMapper {

    public CategoryDTO toCategoryDTO(CategoryModel categoryModel) {
        if (categoryModel == null) {
            return null;
        }

        return CategoryDTO.builder()
                .id(categoryModel.getId())
                .categoryName(categoryModel.getCategoryName())
                .quantityProduct(categoryModel.getListProduct().size())
                .build();
    }
}