package com.apinayami.demo.mapper;

import com.apinayami.demo.dto.response.CouponDto;
import com.apinayami.demo.model.CouponModel;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CouponMapper {

    @Mapping(target = "customerId", source = "customerModel.id")
    CouponDto toDto(CouponModel model);

    List<CouponDto> toDtoList(List<CouponModel> models);
}
