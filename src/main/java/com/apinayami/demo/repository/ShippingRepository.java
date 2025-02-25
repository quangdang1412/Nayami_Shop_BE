package com.apinayami.demo.repository;

import com.apinayami.demo.model.ShippingModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingRepository extends JpaRepository<ShippingModel, Long> {
}