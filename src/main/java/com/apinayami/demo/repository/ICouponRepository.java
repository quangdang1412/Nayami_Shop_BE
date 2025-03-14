package com.apinayami.demo.repository;

import com.apinayami.demo.model.CouponModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICouponRepository extends JpaRepository<CouponModel, Long> {
}