package com.apinayami.demo.mapper;

import com.apinayami.demo.dto.request.DiscountDetailDTO;
import com.apinayami.demo.model.DiscountCampaignModel;
import com.apinayami.demo.model.DiscountDetailModel;
import com.apinayami.demo.repository.IDiscountCampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class DiscountDetailMapper {
    private final IDiscountCampaignRepository discountCampaignRepository;

    public DiscountDetailDTO toDetailDto(DiscountDetailModel user) {
        if (user == null) {
            return null;
        }
        DiscountCampaignModel discountCampaignModel = discountCampaignRepository.findById(user.getDiscountCampaignModel().getId()).orElse(null);
        return DiscountDetailDTO.builder()
                .id(user.getId())
                .percentage(user.getPercentage())
                .startDate(discountCampaignModel != null ? discountCampaignModel.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null)
                .endDate(discountCampaignModel != null ? discountCampaignModel.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null)
                .build();
    }
}
