package com.apinayami.demo.service;

import java.util.List;


import com.apinayami.demo.dto.request.CreateCouponRequest;
import com.apinayami.demo.dto.response.CouponDto;

public interface ICouponService {
    List<CouponDto> getAllCoupons();
    CouponDto getCouponById(String id);
    void deleteCoupon(String id);
    CouponDto createCoupon(CreateCouponRequest request);
    CouponDto updateCoupon(String id, CreateCouponRequest request);
    CouponDto getIdIsActive(String id);

}
