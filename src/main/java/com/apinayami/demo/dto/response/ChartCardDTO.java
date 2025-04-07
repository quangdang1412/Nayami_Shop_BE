package com.apinayami.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChartCardDTO {
    Double totalRevenue;
    Double totalProfit;
    Long quantityProduct;
    Long quantityBillNeedToProcess;

}
