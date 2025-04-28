package com.apinayami.demo.service.Impl;

import com.apinayami.demo.dto.request.FilterOptionDTO;
import com.apinayami.demo.dto.request.OtherConfigurationDTO;
import com.apinayami.demo.dto.request.ProductDTO;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.exception.ResourceNotFoundException;
import com.apinayami.demo.mapper.CategoryMapper;
import com.apinayami.demo.mapper.ProductMapper;
import com.apinayami.demo.model.*;
import com.apinayami.demo.repository.IConfigurationRepository;
import com.apinayami.demo.repository.IOtherConfigurationRepository;
import com.apinayami.demo.repository.IProductRepository;
import com.apinayami.demo.repository.ProductSpecification;
import com.apinayami.demo.service.*;
import com.apinayami.demo.util.Enum.EProductStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
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
    private final ProductMapper productMapper;
    private final PagedResourcesAssembler<ProductDTO> pagedResourcesAssembler;

    public String saveProduct(ProductDTO productRequestDTO, List<MultipartFile> files) {
        List<OtherConfigurationModel> otherConfigurationModelList = new ArrayList<>();
        if (productRequestDTO.getConfigDTO() == null)
            throw new CustomException("Configuration not be flank");
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
                    .orElseThrow(() -> new CustomException("Configuration not found"));

            otherConfigurationRepository.deleteByConfigurationModel(configurationModel.getId());
        } else {
            configurationModel = ConfigurationModel.builder().build();
        }

        configurationModel.setCategoryModel(CategoryMapper.INSTANCE.toCategoryModelWithID(categoryService.findCategoryById(productRequestDTO.getConfigDTO().getCategory())));
        configurationModel.setOtherConfigurationModelList(otherConfigurationModelList);

        for (OtherConfigurationModel otherConfig : otherConfigurationModelList) {
            otherConfig.setConfigurationModel(configurationModel);
        }

        if (productRequestDTO.getConfigDTO().getId() != 0)
            configurationModel.setId(productRequestDTO.getConfigDTO().getId());

        configurationModel = configurationRepository.save(configurationModel);
        otherConfigurationRepository.saveAll(otherConfigurationModelList);

        CategoryModel categoryModel = CategoryMapper.INSTANCE.toCategoryModelWithID(categoryService.findCategoryById(productRequestDTO.getCategoryDTO().getId()));


        ProductModel productModel = ProductModel.builder()
                .productName(productRequestDTO.getName())
                .brandModel(brandService.findBrandById(productRequestDTO.getBrandDTO().getId()))
                .categoryModel(categoryModel)
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

        if (files != null && !files.getFirst().getName().isEmpty()) {
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
    public ProductDTO getProductDTOByID(long id) {
        return productMapper.convertToDTO(productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found")));
    }

    @Override
    public List<ProductDTO> getAllProduct() {
        return productRepository.findAll().stream().map(productMapper::convertToDTO).toList();
    }

    @Override
    public List<ProductDTO> getAllProductDisplayStatusTrue() {
        return productRepository.findProductModelsByDisplayStatusIsTrue().stream().map(productMapper::convertToDTO).toList();
    }

    @Override
    public PagedModel<?> getProductFilter(int pageNo, int pageSize, String sortBy, List<String> brands, List<String> categories, List<Integer> rating, List<Integer> discount, String searchQuery, List<Integer> price) {
        Sort sort = Sort.unsorted();
        if (sortBy != null) {
            if (sortBy.equalsIgnoreCase("desc"))
                sort = Sort.by("unitPrice").descending();
            else
                sort = Sort.by("unitPrice").ascending();
        }
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        Specification<ProductModel> spec = ProductSpecification.filterProducts(brands, categories, rating, discount, searchQuery, price);

        Page<ProductModel> productsPage = productRepository.findAll(spec, pageable);
        return pagedResourcesAssembler.toModel(productsPage.map(productMapper::convertToDTO));
    }

    @Override
    public List<ProductDTO> getProductsHaveDiscount() {
        return productRepository.getProductModelsHaveDiscountModel().stream().filter(ProductModel::isDisplayStatus).map(productMapper::convertToDTO).toList();
    }


    @Override
    public List<ProductDTO> findProductByCategoryId(long id) {
        return productRepository.getProductModelsByCategoryModel_Id(id).stream().filter(ProductModel::isDisplayStatus).map(productMapper::convertToDTO).toList();
    }

    @Override
    public List<ProductDTO> findProductByBrandId(long id) {
        return productRepository.getProductModelsByBrandModelId(id).stream().filter(ProductModel::isDisplayStatus).map(productMapper::convertToDTO).toList();
    }

    @Override
    public List<ProductDTO> getProductOutOfStock() {
        return productRepository.getProductOutOfStock().stream().map(productMapper::convertToDTO).toList();
    }

    @Override
    public Long getQuantityOfProduct() {
        return productRepository.getQuantityOfProduct();
    }

    @Override
    public FilterOptionDTO getFilterOption() {
        FilterOptionDTO filterOptionDTO = new FilterOptionDTO();
        filterOptionDTO.setListBrandDTO(brandService.getAllBrandActive());
        filterOptionDTO.setListCategoryDTO(categoryService.getAllCateActive());
        List<Integer> listRating = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            listRating.add(productRepository.getQuantityProductOfRating(i));
        }
        List<Integer> listDiscount = new ArrayList<>();
        double a = 0, b = 5;
        for (int i = 1; i <= 5; i++) {
            listDiscount.add(discountDetailService.getQuantityProductOfDiscount(a, b));
            a += 5;
            b += 5;
            if (i == 3)
                b = 25;
            else if (i == 4)
                b = 100;
        }
        filterOptionDTO.setListQuantityProductOfRating(listRating);
        filterOptionDTO.setListQuantityProductOfDiscount(listDiscount);

        return filterOptionDTO;
    }

}