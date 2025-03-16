package com.apinayami.demo.mapper;

import com.apinayami.demo.dto.response.CouponDto;
import com.apinayami.demo.model.CouponModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CouponMapper {

    public CouponDto toDto(CouponModel model) {
        if (model == null) {
            return null;
        }
        
        return CouponDto.builder()
                .id(model.getId())
                .content(model.getContent())
                .value(model.getValue())
                .type(model.getType())
                .constraintMoney(model.getConstraintMoney())
                .active(model.isActive())
                .customerId(model.getCustomerModel() != null ? model.getCustomerModel().getId() : null)
                .build();
    }
    
    public List<CouponDto> toDtoList(List<CouponModel> models) {
        if (models == null) {
            return List.of();
        }
        
        return models.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}