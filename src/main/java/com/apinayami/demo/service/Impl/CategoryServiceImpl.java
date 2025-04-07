package com.apinayami.demo.service.Impl;

import com.apinayami.demo.dto.request.CategoryDTO;
import com.apinayami.demo.dto.request.CategoryWithBrandsDTO;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.mapper.BrandMapper;
import com.apinayami.demo.mapper.CategoryMapper;
import com.apinayami.demo.model.BrandModel;
import com.apinayami.demo.model.CategoryModel;
import com.apinayami.demo.model.ProductModel;
import com.apinayami.demo.repository.ICategoryRepository;
import com.apinayami.demo.repository.IProductRepository;
import com.apinayami.demo.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements ICategoryService {

    private final ICategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    private final IProductRepository productRepository;
    private final BrandMapper brandMapper;

    @Override
    public CategoryDTO findCategoryById(long id) {
        return categoryRepository.findById(id).isPresent() ? CategoryMapper.INSTANCE.toCategoryDTO(categoryRepository.findById(id).get()) : null;
    }

    @Override
    public List<CategoryWithBrandsDTO> getAllCategoriesWithBrands() {
        List<CategoryWithBrandsDTO> categoryWithBrandsDTOList = new ArrayList<>();
        List<CategoryModel> categoryModelList = categoryRepository.findAll();
        for (CategoryModel a : categoryModelList) {
            Set<BrandModel> brandModelSet = new HashSet<>();
            List<ProductModel> productModelList = productRepository.getProductModelsByCategoryModel_Id(a.getId());
            for (ProductModel p : productModelList) {
                brandModelSet.add(p.getBrandModel());
            }
            CategoryWithBrandsDTO x = CategoryWithBrandsDTO.builder()
                    .id(a.getId())
                    .categoryName(a.getCategoryName())
                    .brandDTOList(brandModelSet.stream().map(brandMapper::toDetailDto).toList())
                    .build();
            categoryWithBrandsDTOList.add(x);
        }
        return categoryWithBrandsDTOList;
    }

    @Override
    public List<CategoryDTO> getAll() {
        List<CategoryModel> categoryModelList = categoryRepository.findAll();
        return categoryModelList.stream().map(categoryMapper::toCategoryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public String create(CategoryDTO a) {
        try {
            boolean checkExists = categoryRepository.existsByCategoryName(a.getCategoryName());
            if(checkExists){
                throw new CustomException("Category has existed");
            }
            CategoryModel savedCategory = categoryRepository.save(CategoryMapper.INSTANCE.toCategoryModel(a));
            return "Thêm thành công " + savedCategory.getCategoryName();

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new CustomException("Error occurs");
        }
    }

    @Override
    public String update(CategoryDTO a) {
        try {
            CategoryModel found_category = categoryRepository.findById(a.getId()).orElse(null);
            assert found_category != null;
            found_category.setCategoryName(a.getCategoryName());
            categoryRepository.save(found_category);
            return "Cập nhật thành công " + a.getCategoryName();

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new CustomException("Lỗi cập nhật");
        }
    }

    @Override
    public String delete(CategoryDTO a) {
        try {
            CategoryModel found_category = categoryRepository.findById(a.getId()).orElse(null);
            assert found_category != null;
            categoryRepository.delete(found_category);
            return "Xoá thành công " + a.getCategoryName();

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new CustomException("Lỗi khi xoá");
        }
    }
}
