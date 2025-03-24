package com.apinayami.demo.dto.request;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilterOptionDTO implements Serializable {
    List<BrandDTO> listBrandDTO;
    List<CategoryDTO> listCategoryDTO;
    List<Integer> listQuantityProductOfRating;
    List<Integer> listQuantityProductOfDiscount;

}
