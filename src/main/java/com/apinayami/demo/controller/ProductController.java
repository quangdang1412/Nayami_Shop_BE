package com.apinayami.demo.controller;

import com.apinayami.demo.dto.request.ProductDTO;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.exception.ResourceNotFoundException;
import com.apinayami.demo.service.IProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.PagedModel;
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

    @PostMapping()
    public ResponseData<String> addProduct(@RequestPart("productDTO") @Valid String productDTOJson,
                                           @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ProductDTO productDTO = objectMapper.readValue(productDTOJson, ProductDTO.class);

            return new ResponseData<>(HttpStatus.CREATED.value(), "Success", productService.saveProduct(productDTO, files));
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            if (e instanceof CustomException)
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Save failed");
        }
    }

    @PutMapping()
    public ResponseData<String> updateProduct(@RequestPart("productDTO") @Valid String productDTOJson,
                                              @RequestPart("files") List<MultipartFile> files) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            ProductDTO productDTO = objectMapper.readValue(productDTOJson, ProductDTO.class);
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
    public ResponseData<?> changeDisplayStatusProduct(@PathVariable long proID) {
        try {
            log.info("Request update display status : {}", proID);
            return new ResponseData<>(HttpStatus.OK.value(), "Success", productService.delete(proID));

        } catch (Exception e) {
            log.info("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

    }

    @GetMapping()
    public ResponseData<?> getAllProduct() {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get all product successfully", productService.getAllProduct());
        } catch (ResourceNotFoundException e) {
            log.info("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

    }

    @GetMapping("/{proID}")
    public ResponseData<?> getProductById(@PathVariable long proID) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get product successfully", productService.getProductDTOByID(proID));
        } catch (ResourceNotFoundException e) {
            log.info("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

    }

    @GetMapping(value = "/categories/{categoryID}")
    public ResponseData<?> getProductByBrand(@PathVariable long categoryID) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get product successfully", productService.findProductByCategoryId(categoryID));
        } catch (ResourceNotFoundException e) {
            log.info("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

    }

    @GetMapping("/discounts")
    public ResponseData<?> getProductsHaveDiscount() {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get all product successfully", productService.getProductsHaveDiscount());
        } catch (ResourceNotFoundException e) {
            log.info("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

    }

    @GetMapping("/filterOption")
    public ResponseData<?> getFilterOption() {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get all filter option successfully", productService.getFilterOption());
        } catch (ResourceNotFoundException e) {
            log.info("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

    }

    @GetMapping("/filter")
    public ResponseData<?> searchProducts(
            @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "price", required = false) List<Integer> price,
            @RequestParam(name = "search", required = false) String searchQuery,
            @RequestParam(name = "rating", required = false) List<Integer> rating,
            @RequestParam(name = "discounts", required = false) List<Integer> discounts,
            @RequestParam(name = "brands", required = false) List<String> brands,
            @RequestParam(name = "categories", required = false) List<String> categories) {
        try {
            PagedModel<?> productPage = productService.getProductFilter(pageNo, pageSize, sortBy, brands, categories, rating, discounts, searchQuery, price);
            return new ResponseData<>(HttpStatus.OK.value(), "Get product filter successfully", productPage);
        } catch (ResourceNotFoundException e) {
            log.info("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
