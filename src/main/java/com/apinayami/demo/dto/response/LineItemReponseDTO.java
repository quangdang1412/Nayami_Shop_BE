package com.apinayami.demo.dto.response;

import lombok.Data;

@Data
public class LineItemReponseDTO {
    private String productName;
    private String productImage;
    private Double unitPrice;
    private Integer quantity;
    private Double totalPrice;
}
