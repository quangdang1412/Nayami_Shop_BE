package com.apinayami.demo.service;

import com.apinayami.demo.dto.request.ProductDTO;
import com.apinayami.demo.model.ProductModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService {
    String saveProduct(ProductDTO a, List<MultipartFile> files);


    String delete(long a);

    ProductModel getProductByID(long id);

    ProductDTO getProductDTOByID(long id);

    List<ProductDTO> getAllProduct();

    List<ProductDTO> findProductByCategoryId(long id);

    List<ProductDTO> findProductByBrandId(long id);

    List<ProductDTO> getProductOutOfStock();
}
