package com.apinayami.demo.mapper;

import org.mapstruct.Mapper;

import com.apinayami.demo.dto.response.AddressResponseDTO;
import com.apinayami.demo.model.AddressModel;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressResponseDTO toResponseDTO(AddressModel bill);
}
