package com.apinayami.demo.controller;


import com.apinayami.demo.config.JwtConfig;
import com.apinayami.demo.dto.request.CreateCouponRequest;
import com.apinayami.demo.dto.response.CouponDto;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.service.ICouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupons")
public class CouponController {

    private final ICouponService couponService;
    private final JwtConfig jwtConfig;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseData<List<CouponDto>> getAllCoupons() {
        List<CouponDto> coupons = couponService.getAllCoupons();
        return new ResponseData<>(HttpStatus.OK.value(), "Coupons retrieved successfully", coupons);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseData<CouponDto> getCouponById(@PathVariable String id) {
        CouponDto coupon = couponService.getCouponById(id);
        return new ResponseData<>(HttpStatus.OK.value(), "Coupon applied successfully", coupon);
    }

    @GetMapping("/customer/{id}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseData<CouponDto> getCouponByCustomerId(@PathVariable String id) {
        CouponDto coupon = couponService.getIdIsActive(id);
        return new ResponseData<>(HttpStatus.OK.value(), "Coupon applied successfully", coupon);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseData<CouponDto> createCoupon(@Valid @RequestBody CreateCouponRequest request) {
        CouponDto createdCoupon = couponService.createCoupon(request);
        return new ResponseData<>(HttpStatus.CREATED.value(), "Coupon created successfully", createdCoupon);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseData<CouponDto> updateCoupon(
            @PathVariable String id,
            @Valid @RequestBody CreateCouponRequest request) {
        CouponDto updatedCoupon = couponService.updateCoupon(id, request);
        return new ResponseData<>(HttpStatus.OK.value(), "Coupon updated successfully", updatedCoupon);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseData<Void> deleteCoupon(@PathVariable String id) {
        couponService.deleteCoupon(id);
        return new ResponseData<>(HttpStatus.OK.value(), "Coupon deleted successfully", null);
    }

    @GetMapping("/customer")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseData<?> getCouponByCustomer(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String email = extractUserEmail(authHeader);
            if (email == null) {
                return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập để thêm vào giỏ hàng");
            }
            List<CouponDto> coupon = couponService.getCouponsByEmail(email);
            return new ResponseData<>(HttpStatus.OK.value(), "Coupon applied successfully", coupon);
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi khi lấy giỏ hàng", e.getMessage());
        }
    }

    private String extractUserEmail(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        String token = authHeader.substring(7);
        Jwt decodedToken = jwtConfig.decodeToken(token);
        return decodedToken.getSubject();
    }
}
