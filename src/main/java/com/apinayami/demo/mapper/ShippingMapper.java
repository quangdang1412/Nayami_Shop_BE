package com.apinayami.demo.mapper;

import com.apinayami.demo.dto.response.ShippingDTO;
import com.apinayami.demo.model.ShippingModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface ShippingMapper {
    @Mapping(target = "address", source = "addressModel")
    ShippingDTO toShippingDTO(ShippingModel shippingModel);
}
