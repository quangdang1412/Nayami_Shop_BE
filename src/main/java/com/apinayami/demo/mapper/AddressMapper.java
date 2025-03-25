package com.apinayami.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.apinayami.demo.dto.request.AddressRequestDTO;
import com.apinayami.demo.dto.response.AddressResponseDTO;
import com.apinayami.demo.model.AddressModel;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    @Mapping(target = "phone", source = "addressModel.shippingContactNumber")
    @Mapping(target = "addressName", source = "addressModel.detail")
    AddressResponseDTO toResponseDTO(AddressModel addressModel);

    @Mapping(target = "shippingContactNumber", source = "phone")
    @Mapping(target = "detail", source = "addressName")
    @Mapping(target = "customerModel.id", source = "userId")
    AddressModel toModel(AddressRequestDTO addressRequestDTO);

    @Mapping(source = "phone", target = "shippingContactNumber")
    @Mapping(source = "addressName", target = "detail")
    void updateAddressFromDto(AddressRequestDTO dto, @MappingTarget AddressModel entity);


}
