package com.apinayami.demo.repository;

import com.apinayami.demo.model.PromotionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPromotionRepository extends JpaRepository<PromotionModel, Long> {
}
