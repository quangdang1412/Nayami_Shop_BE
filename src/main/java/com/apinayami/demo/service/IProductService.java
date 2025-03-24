package com.apinayami.demo.service;

import com.apinayami.demo.dto.request.FilterOptionDTO;
import com.apinayami.demo.dto.request.ProductDTO;
import com.apinayami.demo.model.ProductModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService {
    String saveProduct(ProductDTO a, List<MultipartFile> files);


    String delete(long a);

    ProductModel getProductByID(long id);

    FilterOptionDTO getFilterOption();

    ProductDTO getProductDTOByID(long id);

    List<ProductDTO> getAllProduct();

    PagedModel<?> getProductFilter(int pageNo, int pageSize, String sortByList, List<String> brands, List<String> categories, List<Integer> rating, List<Integer> discount, String searchQuery);

    List<ProductDTO> getProductsHaveDiscount();

    List<ProductDTO> findProductByCategoryId(long id);

    List<ProductDTO> findProductByBrandId(long id);

    List<ProductDTO> getProductOutOfStock();
}
