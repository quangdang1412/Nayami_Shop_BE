package com.apinayami.demo.repository;

import com.apinayami.demo.model.Product.LaptopModel;
import com.apinayami.demo.model.Product.PhoneModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LaptopRepository extends JpaRepository<LaptopModel, Long> {
}
