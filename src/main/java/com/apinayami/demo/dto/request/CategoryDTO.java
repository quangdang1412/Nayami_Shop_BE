package com.apinayami.demo.dto.request;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO implements Serializable {
    private long id;
    private String categoryName;
    private boolean active;
    private int quantityProduct;
}
