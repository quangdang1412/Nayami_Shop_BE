package com.apinayami.demo.controller;

import com.apinayami.demo.dto.request.BrandDTO;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.mapper.BrandMapper;
import com.apinayami.demo.service.IBrandService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/brands")
@Validated
@Slf4j
public class BrandController {

    private final IBrandService brandService;
    private final BrandMapper brandMapper;

    @Operation(summary = "Get all brands", description = "Returns a list of all available brands")
    @GetMapping
    public ResponseData<List<BrandDTO>> getAllBrands() {
        List<BrandDTO> brands = brandService.getAllBrand();

        return new ResponseData<>(HttpStatus.OK.value(), "Success", brands);
    }

    @Operation(summary = "Get brand by ID", description = "Returns a specific brand by its ID")
    @GetMapping("/{id}")
    public ResponseData<?> getBrandById(@PathVariable Long id) {
        log.info("Request get brand by id: {}", id.toString());
        BrandDTO brand = brandService.findBrandByIdDTO(id);
        if (brand == null) {
            return new ResponseError(HttpStatus.NOT_FOUND.value(), "Brand not found");
        }
        return new ResponseData<>(HttpStatus.OK.value(), "Success", brand);
    }

    @Operation(summary = "Create new brand", description = "Creates a new brand with the provided information")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public ResponseData<String> createBrand(@Valid @RequestBody BrandDTO brandDTO) {
        try {
            log.info("Request add brand: {}", brandDTO.getName());
            brandService.create(brandDTO);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Success", "Thêm thành công " + brandDTO.getName());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            if (e instanceof CustomException)
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Save failed");
        }
    }

    @Operation(summary = "Update brand", description = "Updates an existing brand with the provided information")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public ResponseData<?> updateBrand(@PathVariable Long id, @Valid @RequestBody BrandDTO brandDTO) {
        try {
            log.info("Request update brand: {}", brandDTO.getName());

            brandService.update(brandDTO, id);
            return new ResponseData<>(HttpStatus.OK.value(), "Success", "Cập nhật thành công " + brandDTO.getName());
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            if (e instanceof CustomException)
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update failed");
        }
    }

    @Operation(summary = "Change status brand", description = "Deletes a brand by its ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public ResponseData<?> deleteBrand(@PathVariable Long id) {
        try {
            brandService.delete(id);
            return new ResponseData<>(HttpStatus.OK.value(), "Success",
                    "Thay đổi trạng thái thành công ");
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            if (e instanceof CustomException)
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete failed");
        }
    }
}
