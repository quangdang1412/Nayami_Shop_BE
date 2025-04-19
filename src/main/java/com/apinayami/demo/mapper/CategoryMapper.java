package com.apinayami.demo.mapper;

import com.apinayami.demo.dto.request.CategoryDTO;
import com.apinayami.demo.model.CategoryModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mapping(target = "id", ignore = true)
    CategoryModel toCategoryModel(CategoryDTO dto);

    CategoryModel toCategoryModelWithID(CategoryDTO dto);


    default CategoryDTO toCategoryDTO(CategoryModel categoryModel) {
        if (categoryModel == null) {
            return null;
        }

        return CategoryDTO.builder()
                .id(categoryModel.getId())
                .categoryName(categoryModel.getCategoryName())
                .active(categoryModel.isActive())
                .quantityProduct(categoryModel.getListProduct().size())
                .build();
    }
}
//@Component
//public abstract class CategoryMapper {
//
//    public abstract CategoryModel toCategoryModel(CategoryDTO dto);
//
//    public CategoryDTO toCategoryDTO(CategoryModel categoryModel) {
//        if (categoryModel == null) {
//            return null;
//        }
//
//        return CategoryDTO.builder()
//                .id(categoryModel.getId())
//                .categoryName(categoryModel.getCategoryName())
//                .quantityProduct(categoryModel.getListProduct().size())
//                .build();
//    }
//}