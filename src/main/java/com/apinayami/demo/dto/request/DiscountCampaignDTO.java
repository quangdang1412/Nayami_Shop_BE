package com.apinayami.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class DiscountCampaignDTO implements Serializable {
    private long id;
    @NotBlank(message = "description must be not blank")
    @NotNull
    private String name;
    @NotBlank(message = "description must be not blank")
    @NotNull
    private String description;
    private boolean active;
    @NotNull(message = " Percentage cannot be null")

    @NotNull(message = "Start date cannot be null")
    private LocalDate startDate;
    @NotNull(message = "End date cannot be null")
    private LocalDate endDate;

    private List<DiscountDetailDTO> discountDetailDTOList;
}
