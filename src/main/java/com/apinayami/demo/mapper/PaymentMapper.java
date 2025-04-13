package com.apinayami.demo.mapper;

import com.apinayami.demo.dto.response.PaymentDTO;
import com.apinayami.demo.model.PaymentModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentDTO toPaymentDTO(PaymentModel paymentModel);
}
