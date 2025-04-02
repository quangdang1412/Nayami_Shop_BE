package com.apinayami.demo.controller;

import com.apinayami.demo.dto.response.ChartCardDTO;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.exception.ResourceNotFoundException;
import com.apinayami.demo.repository.IBillRepository;
import com.apinayami.demo.service.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@Validated
@Slf4j
@RequiredArgsConstructor
public class DashboardController {
    private final IProductService productService;
    private final IBillRepository billService;

    @GetMapping()
    public ResponseData<?> getChartCardData() {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get all product successfully",
                    ChartCardDTO.builder()
                            .quantityProduct(productService.getQuantityOfProduct())
                            .build());
        } catch (ResourceNotFoundException e) {
            log.info("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

    }
}
