package com.apinayami.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDetailDTO implements Serializable {
    private long id;

    private double percentage;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Long> productID;
}
