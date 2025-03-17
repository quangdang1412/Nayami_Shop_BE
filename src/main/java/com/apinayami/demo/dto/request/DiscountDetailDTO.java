package com.apinayami.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDetailDTO implements Serializable {
    private long id;

    private double percentage;
    private Date startDate;
    private Date endDate;
    private List<Long> productID;
}
