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
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseData<List<CouponDto>> getAllCoupons() {
        List<CouponDto> coupons = couponService.getAllCoupons();
        return new ResponseData<>(HttpStatus.OK.value(), "Coupons retrieved successfully", coupons);
    }

    @GetMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN') or @securityUtils.isCurrentUser(#id)")
    public ResponseData<CouponDto> getCouponById(@PathVariable String id) {
        CouponDto coupon = couponService.getCouponById(id);
        return new ResponseData<>(HttpStatus.OK.value(), "Coupon retrieved successfully", coupon);
    }

    @GetMapping("/customer/{customerId}")
    // @PreAuthorize("hasRole('ADMIN') or @securityUtils.isCurrentUser(#customerId)")
    public ResponseData<List<CouponDto>> getCouponsByCustomerId(@PathVariable Long customerId) {
        List<CouponDto> coupons = couponService.getCouponsByCustomerId(customerId);
        return new ResponseData<>(HttpStatus.OK.value(), "Coupons retrieved successfully", coupons);
    }

    @GetMapping("/customer/{customerId}/active")
    // @PreAuthorize("hasRole('ADMIN') or @securityUtils.isCurrentUser(#customerId)")
    public ResponseData<List<CouponDto>> getActiveCouponsByCustomerId(@PathVariable Long customerId) {
        List<CouponDto> coupons = couponService.getActiveCouponsByCustomerId(customerId);
        return new ResponseData<>(HttpStatus.OK.value(), "Active coupons retrieved successfully", coupons);
    }

    @GetMapping("/type/{type}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseData<List<CouponDto>> getCouponsByType(@PathVariable ETypeCoupon type) {
        List<CouponDto> coupons = couponService.getCouponsByType(type);
        return new ResponseData<>(HttpStatus.OK.value(), "Coupons retrieved successfully", coupons);
    }

    @PostMapping
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseData<CouponDto> createCoupon(@Valid @RequestBody CreateCouponRequest request) {
        CouponDto createdCoupon = couponService.createCoupon(request);
        return new ResponseData<>(HttpStatus.CREATED.value(), "Coupon created successfully", createdCoupon);
    }

    @PutMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseData<CouponDto> updateCoupon(
            @PathVariable String id,
            @Valid @RequestBody CreateCouponRequest request) {
        CouponDto updatedCoupon = couponService.updateCoupon(id, request);
        return new ResponseData<>(HttpStatus.OK.value(), "Coupon updated successfully", updatedCoupon);
    }

    @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseData<Void> deleteCoupon(@PathVariable String id) {
        couponService.deleteCoupon(id);
        return new ResponseData<>(HttpStatus.OK.value(), "Coupon deleted successfully", null);
    }

    @PutMapping("/{couponId}/assign/{customerId}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseData<CouponDto> assignCouponToCustomer(
            @PathVariable String couponId,
            @PathVariable Long customerId) {
        CouponDto assignedCoupon = couponService.assignCouponToCustomer(couponId, customerId);
        return new ResponseData<>(HttpStatus.OK.value(), "Coupon assigned successfully", assignedCoupon);
    }

    @PutMapping("/{id}/activate")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseData<CouponDto> activateCoupon(@PathVariable String id) {
        CouponDto activatedCoupon = couponService.activateCoupon(id);
        return new ResponseData<>(HttpStatus.OK.value(), "Coupon activated successfully", activatedCoupon);
    }

    @PutMapping("/{id}/deactivate")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseData<CouponDto> deactivateCoupon(@PathVariable String id) {
        CouponDto deactivatedCoupon = couponService.deactivateCoupon(id);
        return new ResponseData<>(HttpStatus.OK.value(), "Coupon deactivated successfully", deactivatedCoupon);
    }
}
