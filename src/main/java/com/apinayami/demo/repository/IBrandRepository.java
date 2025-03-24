package com.apinayami.demo.repository;

import com.apinayami.demo.model.BrandModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBrandRepository extends JpaRepository<BrandModel, Long> {
    BrandModel findBrandById(Long id);

    boolean existsByBrandName(String brandName);
}
