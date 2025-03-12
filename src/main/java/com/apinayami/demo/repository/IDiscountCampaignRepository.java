package com.apinayami.demo.repository;

import com.apinayami.demo.model.DiscountCampaignModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDiscountCampaignRepository extends JpaRepository<DiscountCampaignModel, Long> {
}
