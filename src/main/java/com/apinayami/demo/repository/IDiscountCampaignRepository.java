package com.apinayami.demo.repository;

import com.apinayami.demo.model.DiscountCampaignModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDiscountCampaignRepository extends JpaRepository<DiscountCampaignModel, Long> {
}
