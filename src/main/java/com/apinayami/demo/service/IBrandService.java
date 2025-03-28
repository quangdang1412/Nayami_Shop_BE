package com.apinayami.demo.service;

import com.apinayami.demo.dto.request.BrandDTO;
import com.apinayami.demo.model.BrandModel;

import java.util.List;

public interface IBrandService {
    List<BrandDTO> getAllBrand();

    BrandModel findBrandById(Long id);
    String create (BrandDTO a);
    String update (BrandDTO a);
    String delete (Long a);

}
