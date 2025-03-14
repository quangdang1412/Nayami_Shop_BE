package com.apinayami.demo.mapper;

import org.mapstruct.Mapper;

import com.apinayami.demo.dto.request.BillRequestDTO;
import com.apinayami.demo.dto.response.BillResponseDTO;
import com.apinayami.demo.model.BillModel;


@Mapper(componentModel = "spring")
public interface BillMapper {
    
    BillResponseDTO toResponseDTO(BillModel bill);

    BillModel toEntity(BillRequestDTO billDTO);

}
