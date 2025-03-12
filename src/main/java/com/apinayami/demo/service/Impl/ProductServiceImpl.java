package com.apinayami.demo.service.Impl;

import com.apinayami.demo.dto.request.OtherConfigurationDTO;
import com.apinayami.demo.dto.request.ProductDTO;
import com.apinayami.demo.model.ConfigurationModel;
import com.apinayami.demo.model.OtherConfigurationModel;
import com.apinayami.demo.model.ProductModel;
import com.apinayami.demo.repository.IConfigurationRepository;
import com.apinayami.demo.repository.IOtherConfigurationRepository;
import com.apinayami.demo.repository.IProductRepository;
import com.apinayami.demo.service.IBrandService;
import com.apinayami.demo.service.ICategoryService;
import com.apinayami.demo.service.IDiscountDetailService;
import com.apinayami.demo.service.IProductService;
import com.apinayami.demo.util.Enum.EProductStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements IProductService {
    private final IProductRepository productRepository;
    private final IBrandService brandService;
    private final ICategoryService categoryService;
    private final IDiscountDetailService discountDetailService;
    private final IOtherConfigurationRepository otherConfigurationRepository;
    private final IConfigurationRepository configurationRepository;

    public String create(ProductDTO productRequestDTO, MultipartFile file) {
        List<OtherConfigurationModel> otherConfigurationModelList = new ArrayList<>();
        for (OtherConfigurationDTO a : productRequestDTO.getConfigDTO().getListOtherConfigDTO()) {
            OtherConfigurationModel otherConfig = OtherConfigurationModel.builder()
                    .name(a.getName())
                    .value(a.getValue())
                    .build();
            otherConfigurationModelList.add(otherConfig);

        }
        ConfigurationModel configurationModel = ConfigurationModel.builder()
                .categoryModel(categoryService.findCategoryById(productRequestDTO.getConfigDTO().getCategory()))
                .otherConfigurationModelList(otherConfigurationModelList)
                .build();
        for (OtherConfigurationModel otherConfig : otherConfigurationModelList) {
            otherConfig.setConfigurationModel(configurationModel);
        }

        configurationModel = configurationRepository.save(configurationModel);
        otherConfigurationRepository.saveAll(otherConfigurationModelList);

        ProductModel productModel = ProductModel.builder()
                .productName(productRequestDTO.getName())
                .brandModel(brandService.findBrandById(productRequestDTO.getBrandID()))
                .categoryModel(categoryService.findCategoryById(productRequestDTO.getCategoryID()))
                .description(productRequestDTO.getDescription())
                .discountDetailModel(productRequestDTO.getDiscountID() == 0 ? null : discountDetailService.findDiscountDetailById(productRequestDTO.getDiscountID()))
                .quantity(productRequestDTO.getQuantity())
                .unitPrice(productRequestDTO.getUnitPrice())
                .originalPrice(productRequestDTO.getOriginalPrice())
                .quantity(productRequestDTO.getQuantity())
                .ratingAvg(0)
                .displayStatus(true)
                .productStatus(EProductStatus.valueOf(productRequestDTO.getProductStatus()))
                .configurationModel(configurationModel)
                .listImage(null)
                .build();


        productRepository.save(productModel);
        return productRequestDTO.getName();
    }


    public String update(ProductDTO a) {
        return null;
    }


    public String delete(ProductDTO a) {
        return null;
    }
}
