package com.apinayami.demo.repository;

import com.apinayami.demo.model.DiscountDetailModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface IDiscountDetailRepository extends JpaRepository<DiscountDetailModel, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM DiscountDetailModel o WHERE o.discountCampaignModel.id = :id")
    void deleteByDiscountCampaignModel(long id);

    @Query("SELECT COUNT(d) FROM DiscountDetailModel d WHERE d.percentage >=:from and d.percentage<= :to")
    Integer getQuantityProductOfDiscount(Double from, Double to);
}
