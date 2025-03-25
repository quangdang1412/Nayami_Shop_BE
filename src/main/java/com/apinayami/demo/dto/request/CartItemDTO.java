package com.apinayami.demo.dto.request;

import java.util.List;


import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long id;
    private Long productId;
    private String productName;
    private List<String> listImage;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
}