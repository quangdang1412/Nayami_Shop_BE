package com.apinayami.demo.mapper;

import com.apinayami.demo.dto.request.DiscountCampaignDTO;
import com.apinayami.demo.dto.request.DiscountDetailDTO;
import com.apinayami.demo.model.AbstractEntity;
import com.apinayami.demo.model.DiscountCampaignModel;
import com.apinayami.demo.model.DiscountDetailModel;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Component
public class DiscountCampaignMapper {
    public DiscountCampaignDTO convertToDTO(DiscountCampaignModel discountCampaignModel) {
        List<DiscountDetailDTO> discountDetailDTOList = new ArrayList<>();
        for (DiscountDetailModel x : discountCampaignModel.getListDiscountDetail()) {
            DiscountDetailDTO a = DiscountDetailDTO.builder()
                    .percentage(x.getPercentage())
                    .id(x.getId())
                    .productID(x.getListProduct().stream().map(AbstractEntity::getId).toList())
                    .build();
            discountDetailDTOList.add(a);
        }
        DiscountCampaignDTO a = DiscountCampaignDTO.builder()
                .id(discountCampaignModel.getId())
                .name(discountCampaignModel.getName())
                .description(discountCampaignModel.getDescription())
                .startDate(discountCampaignModel.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .endDate(discountCampaignModel.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .discountDetailDTOList(discountDetailDTOList)
                .build();
        return a;
    }
}
