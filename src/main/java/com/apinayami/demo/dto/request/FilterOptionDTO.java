package com.apinayami.demo.dto.request;

import lombok.*;

import java.util.List;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilterOptionDTO {
    List<BrandDTO> listBrandDTO;
    List<CategoryDTO> listCategoryDTO;
    List<Integer> listQuantityProductOfRating;
    List<Integer> listQuantityProductOfDiscount;

}
