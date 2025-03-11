package com.apinayami.demo.service;

import java.util.List;

import com.apinayami.demo.dto.request.BrandDTO;
import com.apinayami.demo.model.BrandModel;

public interface IBrandService extends IBaseCRUD<BrandModel> {
    List<BrandDTO> getAllBrand();

    BrandModel findBrandById(Long id);
}
