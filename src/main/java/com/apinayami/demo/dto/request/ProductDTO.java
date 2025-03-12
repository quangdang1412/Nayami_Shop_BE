package com.apinayami.demo.dto.request;

import com.apinayami.demo.util.Enum.EProductStatus;
import com.apinayami.demo.util.Validation.EnumValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO implements Serializable {
    @NotNull
    private long id;
    @NotBlank(message = "Name must be not blank")
    @NotNull
    private String name;
    @NotBlank(message = "Description must be not blank")
    @NotNull
    private String description;

    @NotNull
    private long brandID;
    @NotNull
    private long categoryID;
    private long discountID;
    private int ratingAvg;
    private int quantity;
    private double originalPrice;
    private double unitPrice;
    private boolean displayStatus;
    @EnumValue(enumClass = EProductStatus.class, message = "Invalid status.")
    private String productStatus;
    private List<String> listImage;
    private ConfigurationDTO configDTO;
}
