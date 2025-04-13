package com.apinayami.demo.dto.response;

import com.apinayami.demo.dto.request.LineItemDTO;
import com.apinayami.demo.dto.request.UserDTO;
import com.apinayami.demo.model.*;
import com.apinayami.demo.util.Enum.EBillStatus;
import com.apinayami.demo.util.Enum.EPaymentMethod;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillDTO implements Serializable {
    private long id;
    private Double totalPrice;
    private EPaymentMethod paymentMethod;
    private EBillStatus status;
    private String orderNumber;

    private String customerName;
    private String city;
    private PaymentDTO payment;
    private List<LineItemReponseDTO> lineItems;
}
