package com.apinayami.demo.service;

import com.apinayami.demo.dto.request.CategoryDTO;
import com.apinayami.demo.dto.request.CategoryWithBrandsDTO;

import java.util.List;

public interface ICategoryService extends IBaseCRUD<CategoryDTO> {

    List<CategoryDTO> getAll();

    List<CategoryDTO> getAllCateActive();

    CategoryDTO findCategoryById(long id);

    List<CategoryWithBrandsDTO> getAllCategoriesWithBrands(int type);
}
