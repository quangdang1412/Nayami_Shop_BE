package com.apinayami.demo.controller;

import com.apinayami.demo.dto.request.CategoryDTO;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.model.BrandModel;
import com.apinayami.demo.model.CategoryModel;
import com.apinayami.demo.service.ICategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
@Validated
public class CategoryController {
    private final ICategoryService categoryService;

    @GetMapping
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<CategoryModel> getCategoryById(@PathVariable Long id) {
        CategoryModel category = categoryService.findCategoryById(id);
        return category != null ? ResponseEntity.ok(category) : ResponseEntity.notFound().build();
    }
    @SuppressWarnings("unchecked")
    @PostMapping
    public ResponseData<String> addCategory(@RequestBody @Valid CategoryModel category) {
        try {
            categoryService.create(category);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Success", "Thêm thành công " + category.getCategoryName());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Save failed");
        }
    }

    @SuppressWarnings("unchecked")
    @PutMapping("/{id}")
    public ResponseData<String> updateCategory(@PathVariable long id, @RequestBody @Valid CategoryModel category) {
        try {
            CategoryModel updated_category = categoryService.findCategoryById(id);
            updated_category.setCategoryName(category.getCategoryName());
            categoryService.update(updated_category);
            return new ResponseData<>(HttpStatus.OK.value(), "Success", "Cập nhật thành công " + category.getCategoryName());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Save failed");
        }
    }

    @SuppressWarnings("unchecked")
    @DeleteMapping("/{id}")
    public ResponseData<String> deleteCategory(@PathVariable long id) {
        try {
            CategoryModel updated_category = categoryService.findCategoryById(id);
            categoryService.delete(updated_category);
            return new ResponseData<>(HttpStatus.OK.value(), "Success", "Xóa thành công ");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Save failed");
        }
    }


}
