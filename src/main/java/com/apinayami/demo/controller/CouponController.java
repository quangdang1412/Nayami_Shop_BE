package com.apinayami.demo.controller;


import com.apinayami.demo.dto.request.CreateCouponRequest;
import com.apinayami.demo.dto.response.CouponDto;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.service.ICouponService;
import com.apinayami.demo.util.Enum.ETypeCoupon;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupons")
public class CouponController {

    private final ICouponService couponService;

    @GetMapping
    public ResponseData<List<CouponDto>> getAllCoupons() {
        List<CouponDto> coupons = couponService.getAllCoupons();
        return new ResponseData<>(HttpStatus.OK.value(), "Coupons retrieved successfully", coupons);
    }

    @GetMapping("/{id}")
    public ResponseData<CouponDto> getCouponById(@PathVariable String id) {
        CouponDto coupon = couponService.getCouponById(id);
        return new ResponseData<>(HttpStatus.OK.value(), "Coupon applied successfully", coupon);
    }
    @GetMapping("/customer/{id}")
    public ResponseData<CouponDto> getCouponByCustomerId(@PathVariable String id) {
        CouponDto coupon = couponService.getIdIsActive(id);
        return new ResponseData<>(HttpStatus.OK.value(), "Coupon applied successfully", coupon);
    }

    @PostMapping
    public ResponseData<CouponDto> createCoupon(@Valid @RequestBody CreateCouponRequest request) {
        CouponDto createdCoupon = couponService.createCoupon(request);
        return new ResponseData<>(HttpStatus.CREATED.value(), "Coupon created successfully", createdCoupon);
    }

    @PutMapping("/{id}")
    public ResponseData<CouponDto> updateCoupon(
            @PathVariable String id,
            @Valid @RequestBody CreateCouponRequest request) {
        CouponDto updatedCoupon = couponService.updateCoupon(id, request);
        return new ResponseData<>(HttpStatus.OK.value(), "Coupon updated successfully", updatedCoupon);
    }

    @DeleteMapping("/{id}")
    public ResponseData<Void> deleteCoupon(@PathVariable String id) {
        couponService.deleteCoupon(id);
        return new ResponseData<>(HttpStatus.OK.value(), "Coupon deleted successfully", null);
    }



}
