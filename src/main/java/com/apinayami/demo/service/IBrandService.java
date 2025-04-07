package com.apinayami.demo.service;

import com.apinayami.demo.dto.request.BrandDTO;
import com.apinayami.demo.model.BrandModel;

import java.util.List;

public interface IBrandService {
    List<BrandDTO> getAllBrand();

    BrandDTO findBrandByIdDTO(Long id);
    BrandModel findBrandById(Long id);

    String create (BrandDTO a);
    String update (BrandDTO a,Long id);
    String delete (Long a);

}
