package com.apinayami.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandDTO implements Serializable {
    private Long id;

    @NotBlank(message = "Brand name must be not blank")
    private String name;
    
    private boolean active;

    private int quantityProduct;
}