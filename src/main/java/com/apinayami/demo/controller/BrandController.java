package com.apinayami.demo.controller;

import com.apinayami.demo.dto.request.BrandDTO;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.mapper.BrandMapper;
import com.apinayami.demo.model.BrandModel;
import com.apinayami.demo.service.IBrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@Validated
@Slf4j
@RequiredArgsConstructor
public class BrandController {

    private final IBrandService brandService;
    private final BrandMapper brandMapper;

    @GetMapping
    public ResponseEntity<List<BrandDTO>> getAllBrands() {
        List<BrandDTO> brands = brandService.getAllBrand();
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandDTO> getBrandById(@PathVariable Long id) {
        BrandModel brand = brandService.findBrandById(id);
        return brand != null ? ResponseEntity.ok(brandMapper.toDetailDto(brand)) : ResponseEntity.notFound().build();
    }

    @SuppressWarnings("unchecked")
    @PostMapping
    public ResponseData<String> createBrand(@Valid @RequestBody BrandDTO brandDTO) {
        try {
            log.info("Request add brand: {}", brandDTO.getName());
            BrandModel brandModel = new BrandModel();
            brandModel.setBrandName(brandDTO.getName());

            brandService.create(brandModel);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Success", "Thêm thành công " + brandDTO.getName());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            if (e instanceof CustomException)
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Save failed");
        }
    }

    @SuppressWarnings("unchecked")
    @PutMapping("/{id}")
    public ResponseData<String> updateBrand(@PathVariable Long id, @Valid @RequestBody BrandDTO brandDTO) {
        try {
            log.info("Request update brand: {}", brandDTO.getName());
            BrandModel existingBrand = brandService.findBrandById(id);
            if (existingBrand == null) {
                return new ResponseError(HttpStatus.NOT_FOUND.value(), "Brand not found");
            }

            existingBrand.setBrandName(brandDTO.getName());

            brandService.update(existingBrand);
            return new ResponseData<>(HttpStatus.OK.value(), "Success", "Cập nhật thành công " + brandDTO.getName());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            if (e instanceof CustomException)
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update failed");
        }
    }

    @SuppressWarnings("unchecked")
    @DeleteMapping("/{id}")
    public ResponseData<String> deleteBrand(@PathVariable Long id) {
        try {
            BrandModel brand = brandService.findBrandById(id);
            if (brand == null) {
                return new ResponseError(HttpStatus.NOT_FOUND.value(), "Brand not found");
            }
            brandService.delete(brand);
            return new ResponseData<>(HttpStatus.OK.value(), "Success", "Xóa thành công " + brand.getBrandName());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            if (e instanceof CustomException)
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete failed");
        }
    }
}
