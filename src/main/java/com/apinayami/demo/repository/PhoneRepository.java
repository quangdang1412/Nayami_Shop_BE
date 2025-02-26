package com.apinayami.demo.repository;

import com.apinayami.demo.model.CustomerModel;
import com.apinayami.demo.model.Product.BaseProduct;
import com.apinayami.demo.model.Product.PhoneModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneRepository extends JpaRepository<PhoneModel, Long> {
}
