package com.apinayami.demo.mapper;

import org.mapstruct.Mapper;

import com.apinayami.demo.dto.request.CartItemDTO;
import com.apinayami.demo.model.CartItemModel;

@Mapper(componentModel = "spring")
public interface CartItemMapper {


    CartItemModel toCartItemModel(CartItemDTO dto);

    
    CartItemDTO toCartItemDTO(CartItemModel model);
    
}
