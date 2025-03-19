package com.apinayami.demo.repository;

import com.apinayami.demo.dto.response.CouponDto;
import com.apinayami.demo.model.CouponModel;
import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.util.Enum.ETypeCoupon;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ICouponRepository extends JpaRepository<CouponModel, String> {
    List<CouponModel> findByCustomerModel(UserModel customerModel);

    List<CouponModel> findByCustomerModelAndActive(UserModel customerModel, boolean active);

    List<CouponModel> findByType(ETypeCoupon type);

    Optional<CouponModel> findByIdAndCustomerModel(String id, UserModel customerModel);

    Optional<CouponModel> findByIdAndActiveTrue(String id);

}