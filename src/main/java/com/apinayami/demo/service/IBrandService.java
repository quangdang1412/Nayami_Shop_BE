package com.apinayami.demo.service;

import com.apinayami.demo.dto.request.BrandDTO;
import com.apinayami.demo.model.BrandModel;

import java.util.List;

public interface IBrandService extends IBaseCRUD<BrandModel> {
    List<BrandDTO> getAllBrand();

    BrandModel findBrandById(Long id);
}
