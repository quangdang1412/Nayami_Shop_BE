package com.apinayami.demo.controller;

import com.apinayami.demo.dto.request.ProductDTO;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.exception.ResourceNotFoundException;
import com.apinayami.demo.service.IProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Validated
@Slf4j
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;

    @Operation(method = "POST", summary = "Add new product", description = "Send a request via this API to create new product")
    @PostMapping()
    public ResponseData<String> addProduct(@RequestPart("productDTO") @Valid String productDTOJson,
                                           @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ProductDTO productDTO = objectMapper.readValue(productDTOJson, ProductDTO.class);
            log.info("Request add product: {}", productDTO.getName());
            String productName = productService.saveProduct(productDTO, files);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Success", productName);
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            if (e instanceof CustomException)
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Save failed");
        }
    }

    @Operation(method = "PUT", summary = "Update product", description = "Send a request via this API to update product")
    @PutMapping()
    public ResponseData<String> updateProduct(@RequestPart("productDTO") @Valid String productDTOJson,
                                              @RequestPart("files") List<MultipartFile> files) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ProductDTO productDTO = objectMapper.readValue(productDTOJson, ProductDTO.class);
            log.info("Request update product: {}", productDTO.getName());
            String productName = productService.saveProduct(productDTO, files);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Success", productName);
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            if (e instanceof CustomException)
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Save failed");
        }
    }

    @DeleteMapping(value = "{proID}")
    public ResponseData<?> deleteProduct(@PathVariable long proID) {
        try {
            log.info("Request update display status : {}", proID);
            return new ResponseData<>(HttpStatus.OK.value(), "Success", productService.delete(proID));

        } catch (Exception e) {
            log.info("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

    }

    //    @GetMapping("/get-all-product")
//    public ResponseData<?> getAllProduct(@RequestParam(defaultValue = "1", required = false) @Min(value = 1, message = "pageNo must be greater than 1") int pageNo,
//                                         @Valid @Min(value = 10, message = "pageNo must be greater than 10") @RequestParam(defaultValue = "1", required = false) int pageSize,
//                                         @RequestParam(required = false) String sortBy) {
//
//        try {
//            return new ResponseData<>(HttpStatus.OK.value(), "Get user successfully", productService.getAllProductWithSortBy(pageNo, pageSize, sortBy));
//        } catch (ResourceNotFoundException e) {
//            log.info("errorMessage={}", e.getMessage(), e.getCause());
//            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
//        }
//
//    }
    @GetMapping()
    public ResponseData<?> getAllProduct() {

        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get all product successfully", productService.getAllProduct());
        } catch (ResourceNotFoundException e) {
            log.info("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

    }

    @GetMapping(value = "{proID}")
    public ResponseData<?> getProductById(@PathVariable long proID) {

        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get product successfully", productService.getProductDTOByID(proID));
        } catch (ResourceNotFoundException e) {
            log.info("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

    }
}
