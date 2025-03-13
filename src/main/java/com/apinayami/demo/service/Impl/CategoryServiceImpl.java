package com.apinayami.demo.service.Impl;

import com.apinayami.demo.dto.request.CategoryDTO;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.mapper.CategoryMapper;
import com.apinayami.demo.model.CategoryModel;
import com.apinayami.demo.repository.ICategoryRepository;
import com.apinayami.demo.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements ICategoryService {

    private final ICategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    @Override
    public CategoryModel findCategoryById(long id) {
        return categoryRepository.findById(id).isPresent() ? categoryRepository.findById(id).get() : null;
    }

    @Override
    public List<CategoryDTO> getAll() {
        return categoryRepository.findAll().stream().map(categoryMapper::toCategoryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public String create(CategoryModel a) {
        try {
            categoryRepository.save(a);
            return "Thêm thành công " + a.getCategoryName();

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new CustomException("Category has existed");
        }
    }

    @Override
    public String update(CategoryModel a) {
        try {
            categoryRepository.save(a);
            return "Cập nhật thành công " + a.getCategoryName();

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new CustomException("Lỗi cập nhật");
        }
    }

    @Override
    public String delete(CategoryModel a) {
        try {
            categoryRepository.delete(a);
            return "Xoá thành công " + a.getCategoryName();

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new CustomException("Lỗi khi xoá");
        }
    }
}
