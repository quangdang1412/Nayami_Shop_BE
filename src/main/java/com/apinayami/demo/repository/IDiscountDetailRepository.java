package com.apinayami.demo.repository;

import com.apinayami.demo.model.DiscountDetailModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDiscountDetailRepository extends JpaRepository<DiscountDetailModel, Long> {
}
