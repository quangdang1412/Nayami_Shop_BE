package com.apinayami.demo.service.Impl;

import com.apinayami.demo.dto.request.ConfigurationDTO;
import com.apinayami.demo.dto.request.OtherConfigurationDTO;
import com.apinayami.demo.dto.request.ProductDTO;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.exception.ResourceNotFoundException;
import com.apinayami.demo.mapper.BrandMapper;
import com.apinayami.demo.mapper.CategoryMapper;
import com.apinayami.demo.mapper.DiscountDetailMapper;
import com.apinayami.demo.model.ConfigurationModel;
import com.apinayami.demo.model.ImageModel;
import com.apinayami.demo.model.OtherConfigurationModel;
import com.apinayami.demo.model.ProductModel;
import com.apinayami.demo.repository.IConfigurationRepository;
import com.apinayami.demo.repository.IOtherConfigurationRepository;
import com.apinayami.demo.repository.IProductRepository;
import com.apinayami.demo.service.*;
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
    private final IImageService imageService;
    private final BrandMapper brandMapper;
    private final CategoryMapper categoryMapper;
    private final DiscountDetailMapper discountDetailMapper;

    public String saveProduct(ProductDTO productRequestDTO, List<MultipartFile> files) {
        List<OtherConfigurationModel> otherConfigurationModelList = new ArrayList<>();
        for (OtherConfigurationDTO a : productRequestDTO.getConfigDTO().getListOtherConfigDTO()) {
            OtherConfigurationModel otherConfig = OtherConfigurationModel.builder()
                    .name(a.getName())
                    .value(a.getValue())
                    .build();
            otherConfigurationModelList.add(otherConfig);
        }
        ConfigurationModel configurationModel;
        if (productRequestDTO.getConfigDTO().getId() != 0) {
            configurationModel = configurationRepository.findById(productRequestDTO.getConfigDTO().getId())
                    .orElseThrow(() -> new RuntimeException("Configuration not found"));

            otherConfigurationRepository.deleteByConfigurationModel(configurationModel.getId());
        } else {
            configurationModel = ConfigurationModel.builder().build();
        }

        configurationModel.setCategoryModel(categoryService.findCategoryById(productRequestDTO.getConfigDTO().getCategory()));
        configurationModel.setOtherConfigurationModelList(otherConfigurationModelList);

        for (OtherConfigurationModel otherConfig : otherConfigurationModelList) {
            otherConfig.setConfigurationModel(configurationModel);
        }

        if (productRequestDTO.getConfigDTO().getId() != 0)
            configurationModel.setId(productRequestDTO.getConfigDTO().getId());

        configurationModel = configurationRepository.save(configurationModel);
        otherConfigurationRepository.saveAll(otherConfigurationModelList);

        ProductModel productModel = ProductModel.builder()
                .productName(productRequestDTO.getName())
                .brandModel(brandService.findBrandById(productRequestDTO.getBrandDTO().getId()))
                .categoryModel(categoryService.findCategoryById(productRequestDTO.getCategoryDTO().getId()))
                .description(productRequestDTO.getDescription())
                .discountDetailModel(productRequestDTO.getDiscountDTO() == null ? null : discountDetailService.findDiscountDetailById(productRequestDTO.getDiscountDTO().getId()))
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
        if (productRequestDTO.getId() != 0)
            productModel.setId(productRequestDTO.getId());
        productRepository.save(productModel);

        List<ImageModel> imageModelList = new ArrayList<>();

        if (!files.getFirst().getName().isEmpty()) {
            for (MultipartFile file : files) {
                ImageModel imageProduct = null;
                String fileName = imageService.upload(file);
                if (fileName.contains("Something went wrong")) {
                    throw new CustomException("Failed");
                }
                if (!imageService.isPresent(fileName, productModel.getId())) {
                    imageService.addImage(fileName, productModel);
                }
                imageProduct = imageService.findImageByURLAndProductId(fileName, productModel.getId());
                imageModelList.add(imageProduct);
            }
        } else {
            for (String url : productRequestDTO.getListImage()) {
                ImageModel imageProduct = imageService.findImageByURLAndProductId(url, productModel.getId());
                if (imageProduct != null) {
                    imageModelList.add(imageProduct);
                }
            }

        }
        productModel.setListImage(imageModelList);
        productRepository.save(productModel);
        return productRequestDTO.getName();
    }

    public String delete(long a) {
        ProductModel productModel = getProductByID(a);
        productModel.setDisplayStatus(!productModel.isDisplayStatus());
        productRepository.save(productModel);
        return productModel.getId().toString();
    }

    @Override
    public ProductModel getProductByID(long id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @Override
    public List<ProductDTO> getAllProduct() {
//        for (ProductModel productModel : productRepository.findAll()) {
//            if (productModel.getDiscountDetailModel() != null && productModel.getDiscount().getEndDate().toLocalDate().isBefore(LocalDate.now())) {
//                productModel.setDiscount(null);
//                productRepository.save(productModel);
//            }
//        }
        return productRepository.findAll().stream().map(v -> convertToDTO(v)).toList();
    }

    @Override
    public List<ProductDTO> findProductByCategoryId(long id) {
        return productRepository.getProductModelsByCategoryModel_Id(id).stream().map(v -> convertToDTO(v)).toList();
    }

    @Override
    public List<ProductDTO> findProductByBrandId(long id) {
        return productRepository.getProductModelsByBrandModelId(id).stream().map(v -> convertToDTO(v)).toList();
    }

    @Override
    public List<ProductDTO> getProductOutOfStock() {
        return productRepository.getProductOutOfStock().stream().map(v -> convertToDTO(v)).toList();
    }

    private ProductDTO convertToDTO(ProductModel productModel) {
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

//    @Override
//    public Page<ProductModel> getProductForPage(Integer pageNumber, String categoryID, String brandID, String sortBy, String searchQuery) {
//        Pageable pageable = null;
//        if (sortBy == null)
//            pageable = PageRequest.of(pageNumber - 1, 9);
//
//        else {
//            if (sortBy.equalsIgnoreCase("asc")) {
//                pageable = PageRequest.of(pageNumber - 1, 9, Sort.by(Sort.Direction.ASC, "unitPrice"));
//            } else {
//                pageable = PageRequest.of(pageNumber - 1, 9, Sort.by(Sort.Direction.DESC, "unitPrice"));
//            }
//        }
//
//        return productRepository.getProductForPage(
//                categoryID != null ? "%" + categoryID + "%" : null,
//                brandID != null ? "%" + brandID + "%" : null,
//                searchQuery != null ? "%" + searchQuery + "%" : null,
//                pageable
//        );
//    }

}
