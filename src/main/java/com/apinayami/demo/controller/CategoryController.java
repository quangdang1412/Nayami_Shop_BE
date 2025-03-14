package com.apinayami.demo.controller;

import com.apinayami.demo.dto.request.CategoryDTO;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.mapper.CategoryMapper;
import com.apinayami.demo.model.CategoryModel;
import com.apinayami.demo.service.ICategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Category", description = "Category management APIs")
public class CategoryController {
    private final ICategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Operation(summary = "Get all categories", description = "Retrieves a list of all categories")
    @GetMapping
    public ResponseData<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAll();
        return new ResponseData<>(HttpStatus.OK.value(), "Success", categories);
    }

    @Operation(summary = "Get category by ID", description = "Get a category by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category found"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{id}")
    public ResponseData<CategoryDTO> getCategoryById(@PathVariable Long id) {
        CategoryModel category = categoryService.findCategoryById(id);
        return category != null 
            ? new ResponseData<>(HttpStatus.OK.value(), "Success", categoryMapper.toCategoryDTO(category)) 
            : new ResponseData<>(HttpStatus.NOT_FOUND.value(), "Category not found", null);
    }

    @Operation(summary = "Create a new category", description = "Creates a new category")
    @SuppressWarnings("unchecked")
    @PostMapping
    public ResponseData<String> addCategory(@RequestBody @Valid CategoryModel category) {
        try {
            categoryService.create(category);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Success",
                    "Thêm thành công " + category.getCategoryName());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Save failed");
        }
    }

    @Operation(summary = "Update a category", description = "Updates an existing category by ID")
    @SuppressWarnings("unchecked")
    @PutMapping("/{id}")
    public ResponseData<String> updateCategory(@PathVariable long id, @RequestBody @Valid CategoryModel category) {
        try {
            CategoryModel updated_category = categoryService.findCategoryById(id);
            updated_category.setCategoryName(category.getCategoryName());
            categoryService.update(updated_category);
            return new ResponseData<>(HttpStatus.OK.value(), "Success",
                    "Cập nhật thành công " + category.getCategoryName());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Save failed");
        }
    }

    @Operation(summary = "Delete a category", description = "Deletes a category by ID")
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