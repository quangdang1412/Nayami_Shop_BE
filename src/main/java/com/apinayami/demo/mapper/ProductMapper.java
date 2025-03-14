package com.apinayami.demo.mapper;

import com.apinayami.demo.dto.request.ConfigurationDTO;
import com.apinayami.demo.dto.request.OtherConfigurationDTO;
import com.apinayami.demo.dto.request.ProductDTO;
import com.apinayami.demo.model.ImageModel;
import com.apinayami.demo.model.OtherConfigurationModel;
import com.apinayami.demo.model.ProductModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final BrandMapper brandMapper;
    private final CategoryMapper categoryMapper;
    private final DiscountDetailMapper discountDetailMapper;

    public ProductDTO convertToDTO(ProductModel productModel) {
        ConfigurationDTO configurationDTO = new ConfigurationDTO();
        configurationDTO.setCategory(productModel.getCategoryModel().getId());
        List<OtherConfigurationDTO> otherConfigurationDTOList = new ArrayList<>();
        for (OtherConfigurationModel a : productModel.getConfigurationModel().getOtherConfigurationModelList()) {
            otherConfigurationDTOList.add(new OtherConfigurationDTO(a.getId(), a.getName(), a.getValue()));
        }
        configurationDTO.setListOtherConfigDTO(otherConfigurationDTOList);
        configurationDTO.setId(productModel.getConfigurationModel().getId());
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(productModel.getId());
        productDTO.setName(productModel.getProductName());
        productDTO.setBrandDTO(brandMapper.toDetailDto(productModel.getBrandModel()));
        productDTO.setCategoryDTO(categoryMapper.toCategoryDTO(productModel.getCategoryModel()));
        productDTO.setDiscountDTO(productModel.getDiscountDetailModel() != null ? discountDetailMapper.toDetailDto(productModel.getDiscountDetailModel()) : null);
        productDTO.setListImage(productModel.getListImage().stream().map(ImageModel::getUrl).toList());
        productDTO.setDescription(productModel.getDescription());
        productDTO.setUnitPrice(productModel.getUnitPrice());
        productDTO.setQuantity(productModel.getQuantity());
        productDTO.setDisplayStatus(productModel.isDisplayStatus());
        productDTO.setUnitPrice(productModel.getUnitPrice());
        productDTO.setOriginalPrice(productModel.getOriginalPrice());
        productDTO.setRatingAvg(productModel.getRatingAvg());
        productDTO.setProductStatus(productModel.getProductStatus().toString());
        productDTO.setConfigDTO(configurationDTO);
        return productDTO;
    }
}
