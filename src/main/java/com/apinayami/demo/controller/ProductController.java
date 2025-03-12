package com.apinayami.demo.controller;

import com.apinayami.demo.dto.request.ProductDTO;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.service.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@Validated
@Slf4j
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;

    @Operation(method = "POST", summary = "Add new product", description = "Send a request via this API to create new user")
    @PostMapping("/add-product")
    public ResponseData<String> addUser(@Valid @RequestBody ProductDTO productDTO) {
        try {
            log.info("Request add product: {}", productDTO.getName());
            String productName = productService.create(productDTO, null);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Success", productName);
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            if (e instanceof CustomException)
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Save failed");
        }
    }
}
