package com.apinayami.demo.controller;

import com.apinayami.demo.dto.request.BrandDTO;
import com.apinayami.demo.dto.request.PromotionDTO;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.model.CategoryModel;
import com.apinayami.demo.model.PromotionModel;
import com.apinayami.demo.service.IPromotionService;
import com.apinayami.demo.service.Impl.PromotionServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/promotions")
@Validated
public class PromotionController {
    private final IPromotionService promotionService;
    @PostMapping
    public ResponseData<String> addPromotion(@RequestBody @Valid PromotionModel promotionModel) {
        try {
            promotionService.create(promotionModel);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Success", "Thêm thành công " + promotionModel.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Save failed");
        }
    }
    @GetMapping
    public ResponseData<List<PromotionModel>> getAllPromotions() {
        List<PromotionModel> promotions = promotionService.getAllPromotions();

        return new ResponseData<>(HttpStatus.OK.value(), "Success", promotions);
    }
    @DeleteMapping("/{id}")
    public ResponseData<String> deleteCategory(@PathVariable long id) {
        try {
            PromotionModel deleted_promotion = promotionService.getPromotionById(id);
            promotionService.delete(deleted_promotion);
            return new ResponseData<>(HttpStatus.OK.value(), "Success", "Xóa thành công ");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Save failed");
        }
    }
    @PutMapping("/{id}")
    public ResponseData<String> updateCategory(@PathVariable long id, @RequestBody @Valid PromotionModel promotionModel) {
        try {
            PromotionModel updated_category = promotionService.getPromotionById(id);
            updated_category.setTitle(promotionModel.getTitle());
            updated_category.setDescription(promotionModel.getDescription());
            updated_category.setStartDate(promotionModel.getStartDate());
            updated_category.setEndDate(promotionModel.getEndDate());
            promotionService.update(updated_category);
            return new ResponseData<>(HttpStatus.OK.value(), "Success", "Cập nhật thành công " + updated_category.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Save failed");
        }
    }
}
