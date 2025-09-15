package com.apinayami.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductBestSellingDTO implements Serializable {
    private Long id;
    private String url;
    private String name;
    private Double unitPrice;
    private Integer quantity;
    private Integer quantitySold;
}
