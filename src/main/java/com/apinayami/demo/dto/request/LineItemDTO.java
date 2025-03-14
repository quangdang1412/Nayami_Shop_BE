package com.apinayami.demo.dto.request;
import lombok.*;
@Data
public class LineItemDTO {
    private Long id;
    private Long productId;
    private Integer quantity;
    private Double unitPrice;
}