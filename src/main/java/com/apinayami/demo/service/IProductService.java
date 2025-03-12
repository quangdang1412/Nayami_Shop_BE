package com.apinayami.demo.service;

import com.apinayami.demo.dto.request.ProductDTO;
import org.springframework.web.multipart.MultipartFile;

public interface IProductService {
    String create(ProductDTO a, MultipartFile file);

    String update(ProductDTO a);

    String delete(ProductDTO a);
}
