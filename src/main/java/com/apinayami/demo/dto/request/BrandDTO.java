package com.apinayami.demo.dto.request;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandDTO implements Serializable {
    private Long id;

    @NotBlank(message = "Brand name must be not blank")
    private String name;

    private List<String> productNames;
}