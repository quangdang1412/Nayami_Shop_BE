package com.apinayami.demo.mapper;

import java.util.List;

import com.apinayami.demo.dto.response.BillDTO;
import com.apinayami.demo.dto.response.BillDetailDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import com.apinayami.demo.dto.request.BillRequestDTO;
import com.apinayami.demo.dto.response.BillResponseDTO;
import com.apinayami.demo.dto.response.HistoryOrderDTO;
import com.apinayami.demo.model.BillModel;


@Mapper(componentModel = "spring",
        uses = {
                LineItemMapper.class,
                CouponMapper.class,
                UserMapper.class,
                PaymentMapper.class,
                AddressMapper.class,
                ShippingMapper.class,
        })
public interface BillMapper {

    BillResponseDTO toResponseDTO(BillModel bill);

    BillModel toEntity(BillRequestDTO billDTO);
    
    @Mapping(target = "items", source = "items") 
    @Mapping(target = "totalPrice", source = "totalPrice")
    @Mapping(target = "paymentStatus", source = "paymentModel.paymentStatus")
    HistoryOrderDTO toDTO(BillModel billModel);

    List<HistoryOrderDTO> toDTOList(List<BillModel> billModels);

    @Mapping(target = "lineItems", source = "items")
    @Mapping(target = "customerName", source = "customerModel.username")
    @Mapping(target = "payment", source = "paymentModel")
    @Mapping(target = "city", source = "shippingModel.addressModel.province")
    BillDTO toBillDTOFull (BillModel billModel);

    @Mapping(target = "items", source = "items")
    @Mapping(target = "coupon", source = "couponModel")
    @Mapping(target = "customer", source = "customerModel")
    @Mapping(target = "payment", source = "paymentModel")
    @Mapping(target = "shipping", source = "shippingModel")
    @Mapping(target = "createdAt", source = "createdAt")
    BillDetailDTO toBillDetailDTO(BillModel billModel);

}
