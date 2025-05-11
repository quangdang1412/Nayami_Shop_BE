package com.apinayami.demo.controller;

import com.apinayami.demo.dto.request.DashBoardDateDTO;
import com.apinayami.demo.dto.response.ChartCardDTO;
import com.apinayami.demo.dto.response.DashBoardResponseDTO;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.exception.ResourceNotFoundException;
import com.apinayami.demo.service.IBillService;
import com.apinayami.demo.service.IProductService;
import com.apinayami.demo.util.Enum.EBillStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/dashboard")
@Validated
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class DashboardController {
    private final IProductService productService;
    private final IBillService billService;

    @GetMapping("/chartCard")
    public ResponseData<?> getChartCardData() {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get all product successfully",
                    ChartCardDTO.builder()
                            .quantityProduct(productService.getQuantityOfProduct())
                            .quantityBillNeedToProcess(billService.countBillsByStatus(EBillStatus.PENDING) + billService.countBillsByStatus(EBillStatus.CONFIRMED))
                            .totalProfit(billService.totalProfit(EBillStatus.COMPLETED))
                            .totalRevenue(billService.totalRevenue(EBillStatus.COMPLETED))
                            .build());
        } catch (ResourceNotFoundException e) {
            log.info("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PostMapping("/revenueByTime")
    public ResponseData<?> getRevenueByTime(@RequestBody DashBoardDateDTO dashboardDateDTO) {
        try {
            LocalDate startDate = dashboardDateDTO.getStartDate();
            LocalDate endDate = dashboardDateDTO.getEndDate();
            DashBoardResponseDTO dashboardResponse = billService.getRevenueOrProfitByTime(startDate, endDate, EBillStatus.COMPLETED, 0);
            return new ResponseData<>(HttpStatus.OK.value(), "Get all product successfully", dashboardResponse);
        } catch (ResourceNotFoundException e) {
            log.info("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PostMapping("/profitByTime")
    public ResponseData<?> getProfitByTime(@RequestBody DashBoardDateDTO dashboardDateDTO) {
        try {
            LocalDate startDate = dashboardDateDTO.getStartDate();
            LocalDate endDate = dashboardDateDTO.getEndDate();
            DashBoardResponseDTO dashboardResponse = billService.getRevenueOrProfitByTime(startDate, endDate, EBillStatus.COMPLETED, 1);
            return new ResponseData<>(HttpStatus.OK.value(), "Get all product successfully", dashboardResponse);
        } catch (ResourceNotFoundException e) {
            log.info("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/productOutOfStock")
    public ResponseData<?> getProductOutOfStock() {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get all product successfully", productService.getProductOutOfStock());
        } catch (ResourceNotFoundException e) {
            log.info("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PostMapping("/productBestSelling")
    public ResponseData<?> getProductBestSelling(@RequestBody DashBoardDateDTO dashboardDateDTO) {
        try {
            LocalDate startDate = dashboardDateDTO.getStartDate();
            LocalDate endDate = dashboardDateDTO.getEndDate();
            return new ResponseData<>(HttpStatus.OK.value(), "Get all product successfully", billService.getProductBestSellingByTime(startDate, endDate, EBillStatus.COMPLETED));
        } catch (ResourceNotFoundException e) {
            log.info("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
