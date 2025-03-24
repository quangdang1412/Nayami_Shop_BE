package com.apinayami.demo.mapper;

import com.apinayami.demo.dto.response.LineItemReponseDTO;
import com.apinayami.demo.model.ImageModel;
import com.apinayami.demo.model.LineItemModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LineItemMapper {

    @Mapping(source = "productModel.productName", target = "productName")
    @Mapping(source = "productModel.listImage", target = "productImage", qualifiedByName = "getFirstImage")
    LineItemReponseDTO toDto(LineItemModel lineItem);

    List<LineItemReponseDTO> toDtoList(List<LineItemModel> lineItems);

    @Named("getFirstImage")
    default String getFirstImage(List<ImageModel> images) {
        return (images != null && !images.isEmpty()) ? images.get(0).getUrl() : null;
    }
}
