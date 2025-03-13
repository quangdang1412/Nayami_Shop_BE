package com.apinayami.demo.mapper;

import com.apinayami.demo.dto.request.DiscountDetailDTO;
import com.apinayami.demo.model.DiscountDetailModel;
import org.springframework.stereotype.Component;

@Component
public class DiscountDetailMapper {
    public DiscountDetailDTO toDetailDto(DiscountDetailModel user) {
        if (user == null) {
            return null;
        }
        return DiscountDetailDTO.builder()
                .id(user.getId())
                .percentage(user.getPercentage())
                .build();
    }
}
