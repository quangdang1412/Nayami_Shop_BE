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
    public ResponseData<String> addPromotion(@RequestBody @Valid PromotionDTO promotionDTO) {
        try {
            String result = promotionService.create(promotionDTO);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Success", result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Save failed");
        }
    }
    @GetMapping
    public ResponseData<?> getAllPromotions() {
        List<PromotionDTO> promotionList = promotionService.getAllPromotions();
        return new ResponseData<>(HttpStatus.OK.value(), "Success", promotionList);
    }

    @GetMapping("/{id}")
    public ResponseData<?> getPromotionById(@PathVariable long id) {
        PromotionDTO promotion = promotionService.getPromotionById(id);

        return new ResponseData<>(HttpStatus.OK.value(), "Success", promotion);
    }
    @DeleteMapping("/{id}")
    public ResponseData<String> deletePromotion(@PathVariable long id) {
        try {
            PromotionDTO deleted_promotion = promotionService.getPromotionById(id);
            System.out.println(deleted_promotion.toString());
            String result = promotionService.delete(deleted_promotion);
            return new ResponseData<>(HttpStatus.OK.value(), "Success", result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Save failed");
        }
    }
    @PutMapping("/{id}")
    public ResponseData<String> updatePromotion(@PathVariable long id, @RequestBody @Valid PromotionDTO promotionDTO) {
        try {
            PromotionDTO update_promotion = promotionService.getPromotionById(id);
            update_promotion.setTitle(promotionDTO.getTitle());
            update_promotion.setDescription(promotionDTO.getDescription());
            update_promotion.setStartDate(promotionDTO.getStartDate());
            update_promotion.setEndDate(promotionDTO.getEndDate());
            update_promotion.setDisplayStatus(promotionDTO.isDisplayStatus());
            String result = promotionService.update(update_promotion);
            return new ResponseData<>(HttpStatus.OK.value(), "Success", result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Save failed");
        }
    }
}
