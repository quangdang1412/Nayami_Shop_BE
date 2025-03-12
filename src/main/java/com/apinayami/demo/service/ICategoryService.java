package com.apinayami.demo.service;

import com.apinayami.demo.dto.request.CategoryDTO;
import com.apinayami.demo.model.CategoryModel;

import java.util.List;

public interface ICategoryService extends IBaseCRUD<CategoryModel> {

    List<CategoryDTO> getAll();
    CategoryModel findById(long id);

}
