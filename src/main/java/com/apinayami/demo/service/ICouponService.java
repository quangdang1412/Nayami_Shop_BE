package com.apinayami.demo.service;

import java.util.List;


import com.apinayami.demo.dto.request.CreateCouponRequest;
import com.apinayami.demo.dto.response.CouponDto;
import com.apinayami.demo.util.Enum.ETypeCoupon;

public interface ICouponService {
    List<CouponDto> getAllCoupons();
    CouponDto getCouponById(String id);
    void deleteCoupon(String id);
    CouponDto createCoupon(CreateCouponRequest request);
    CouponDto updateCoupon(String id, CreateCouponRequest request);
    CouponDto assignCouponToCustomer(String id, Long customerId);
    CouponDto activateCoupon(String id);
    CouponDto deactivateCoupon(String id);
    List<CouponDto> getCouponsByCustomerId(Long customerId);
    List<CouponDto> getActiveCouponsByCustomerId(Long customerId);
    List<CouponDto> getCouponsByType(ETypeCoupon type);
    
}
