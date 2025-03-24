package com.apinayami.demo.dto.response;

import java.util.Date;
import java.util.List;

import com.apinayami.demo.util.Enum.EBillStatus;
import com.apinayami.demo.util.Enum.EPaymentMethod;
import com.apinayami.demo.util.Enum.EPaymentStatus;

import lombok.Data;


@Data
public class HistoryOrderDTO  {
    private Long id;
    private Double totalPrice;
    private List<LineItemReponseDTO> items;
    private EPaymentMethod paymentMethod;
    private EBillStatus status;
    private String orderNumber;
    private Date createdAt;
    private EPaymentStatus paymentStatus;
}
