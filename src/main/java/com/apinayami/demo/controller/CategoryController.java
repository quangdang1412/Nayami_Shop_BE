package com.apinayami.demo.controller;

import com.apinayami.demo.dto.request.CategoryDTO;
import com.apinayami.demo.dto.request.CategoryWithBrandsDTO;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.service.ICategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseData<?> getAllCategories() {
        List<CategoryDTO> categoryList = categoryService.getAll();
        return new ResponseData<>(HttpStatus.OK.value(), "Success", categoryList);
    }

    @Operation(summary = "Get category by ID", description = "Get a category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{id}")
    public ResponseData<?> getCategoryById(@PathVariable Long id) {
        CategoryDTO category = categoryService.findCategoryById(id);
        return category != null
                ? new ResponseData<>(HttpStatus.OK.value(), "Success", category)
                : new ResponseData<>(HttpStatus.NOT_FOUND.value(), "Category not found", null);
    }

    @Operation(summary = "Create a new category", description = "Creates a new category")
    @PostMapping
    public ResponseData<?> addCategory(@RequestBody @Valid CategoryDTO category) {
        try {
            String result = categoryService.create(category);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Success", result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Save failed");
        }
    }

    @Operation(summary = "Update a category", description = "Updates an existing category by ID")
    @PutMapping("/{id}")
    public ResponseData<?> updateCategory(@PathVariable long id, @RequestBody @Valid CategoryDTO category) {
        try {
            System.out.println(category.isActive());
            CategoryDTO updated_category = categoryService.findCategoryById(id);
            updated_category.setCategoryName(category.getCategoryName());
            updated_category.setActive(category.isActive());
            String result = categoryService.update(updated_category);
            return new ResponseData<>(HttpStatus.OK.value(), "Success", result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Save failed");
        }
    }

    @Operation(summary = "Delete a category", description = "Deletes a category by ID")
    @DeleteMapping("/{id}")
    public ResponseData<?> deleteCategory(@PathVariable long id) {
        try {
            CategoryDTO updated_category = categoryService.findCategoryById(id);
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
        List<CategoryWithBrandsDTO> categories = categoryService.getAllCategoriesWithBrands(0);
        return new ResponseData<>(HttpStatus.OK.value(), "Success", categories);
    }

    @Operation(summary = "Get all categories active with brands", description = "Retrieves a list of all categories active with brands")
    @GetMapping("/active/brands")
    public ResponseData<List<CategoryWithBrandsDTO>> getAllCategoriesActiveWithBrands() {
        List<CategoryWithBrandsDTO> categories = categoryService.getAllCategoriesWithBrands(1);
        return new ResponseData<>(HttpStatus.OK.value(), "Success", categories);
    }

}

