package com.apinayami.demo.repository;

import com.apinayami.demo.model.BrandModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<BrandModel, Long> {
}
