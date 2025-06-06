package com.apinayami.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryWithBrandsDTO implements Serializable {
    private long id;
    private String categoryName;
    private List<BrandDTO> brandDTOList;
}
