package com.apinayami.demo.controller;

import com.apinayami.demo.dto.request.DiscountCampaignDTO;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.exception.ResourceNotFoundException;
import com.apinayami.demo.service.IDiscountCampaignService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/discounts")
@Validated
@Slf4j
public class DiscountCampaignController {
    private final IDiscountCampaignService discountCampaignService;

    @Operation(method = "POST", summary = "Add new discountCampaign", description = "Send a request via this API to create new discountCampaign")
    @PostMapping()
    public ResponseData<?> addDiscountCampaign(@Valid @RequestBody DiscountCampaignDTO discountCampaignDTO) {
        try {
            log.info("Request add discountCampaign: {}", discountCampaignDTO.getName());
            return new ResponseData<>(HttpStatus.CREATED.value(), "Success", discountCampaignService.create(discountCampaignDTO));
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            if (e instanceof CustomException)
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Save failed");
        }
    }

    @Operation(method = "PUT", summary = "Update discountCampaign", description = "Send a request via this API to update discountCampaign")
    @PutMapping()
    public ResponseData<?> updateDiscountCampaign(@Valid @RequestBody DiscountCampaignDTO discountCampaignDTO) {
        try {
            log.info("Request add discountCampaign: {}", discountCampaignDTO.getName());
            return new ResponseData<>(HttpStatus.CREATED.value(), "Success", discountCampaignService.create(discountCampaignDTO));
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            if (e instanceof CustomException)
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Save failed");
        }
    }

    @DeleteMapping(value = "{proID}")
    public ResponseData<?> changeStatusDiscountCampaign(@PathVariable long proID) {
        try {
            log.info("Request update active : {}", proID);
            return new ResponseData<>(HttpStatus.OK.value(), "Success", discountCampaignService.delete(proID));
        } catch (Exception e) {
            log.info("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

    }

    @GetMapping()
    public ResponseData<?> getAllDiscountCampaign() {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get all DiscountCampaign successfully", discountCampaignService.getAllDiscountCampaign());
        } catch (ResourceNotFoundException e) {
            log.info("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

    }

    @GetMapping(value = "{ID}")
    public ResponseData<?> getDiscountCampaignById(@PathVariable long ID) {

        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get DiscountCampaign successfully", discountCampaignService.getDiscountCampaignDTOById(ID));
        } catch (ResourceNotFoundException e) {
            log.info("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

    }
}
