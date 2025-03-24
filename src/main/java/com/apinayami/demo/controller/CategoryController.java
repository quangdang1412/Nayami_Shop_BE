package com.apinayami.demo.controller;

import com.apinayami.demo.dto.request.CategoryDTO;
import com.apinayami.demo.dto.request.CategoryWithBrandsDTO;
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

    @Operation(summary = "Get category by ID", description = "Get a category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryModel> getCategoryById(@PathVariable Long id) {
        CategoryModel category = categoryService.findCategoryById(id);
        return category != null
                ? new ResponseData<>(HttpStatus.OK.value(), "Success", categoryMapper.toCategoryDTO(category))
                : new ResponseData<>(HttpStatus.NOT_FOUND.value(), "Category not found", null);
    }

    @Operation(summary = "Create a new category", description = "Creates a new category")
    @PostMapping
    public ResponseData<?> addCategory(@RequestBody @Valid CategoryModel category) {
        try {
            categoryService.create(category);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Success", "Thêm thành công " + category.getCategoryName());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Save failed");
        }
    }

    @Operation(summary = "Update a category", description = "Updates an existing category by ID")
    @PutMapping("/{id}")
    public ResponseData<?> updateCategory(@PathVariable long id, @RequestBody @Valid CategoryModel category) {
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

    @Operation(summary = "Delete a category", description = "Deletes a category by ID")
    @DeleteMapping("/{id}")
    public ResponseData<?> deleteCategory(@PathVariable long id) {
        try {
            CategoryModel updated_category = categoryService.findCategoryById(id);
            categoryService.delete(updated_category);
            return new ResponseData<>(HttpStatus.OK.value(), "Success", "Xóa thành công ");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Save failed");
        }
    }

    @Operation(summary = "Get all categories with brands", description = "Retrieves a list of all categories with brands")
    @GetMapping("/brands")
    public ResponseData<List<CategoryWithBrandsDTO>> getAllCategoriesWithBrands() {
        List<CategoryWithBrandsDTO> categories = categoryService.getAllCategoriesWithBrands();
        return new ResponseData<>(HttpStatus.OK.value(), "Success", categories);
    }
}

