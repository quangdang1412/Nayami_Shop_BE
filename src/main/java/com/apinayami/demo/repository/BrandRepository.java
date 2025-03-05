package com.apinayami.demo.repository;

import com.apinayami.demo.model.BrandModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBrandRepository extends JpaRepository<BrandModel,Long> {
}
