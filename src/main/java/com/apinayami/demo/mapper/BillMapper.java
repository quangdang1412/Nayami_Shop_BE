package com.apinayami.demo.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import com.apinayami.demo.dto.request.BillRequestDTO;
import com.apinayami.demo.dto.response.BillResponseDTO;
import com.apinayami.demo.dto.response.HistoryOrderDTO;
import com.apinayami.demo.model.BillModel;


@Mapper(componentModel = "spring", uses = { LineItemMapper.class }) 
public interface BillMapper {
    
    BillResponseDTO toResponseDTO(BillModel bill);

    BillModel toEntity(BillRequestDTO billDTO);
    
    @Mapping(target = "items", source = "items") 
    @Mapping(target = "totalPrice", source = "totalPrice")
    HistoryOrderDTO toDTO(BillModel billModel);

    List<HistoryOrderDTO> toDTOList(List<BillModel> billModels);

}
