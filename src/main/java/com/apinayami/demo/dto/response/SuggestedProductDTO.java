package com.apinayami.demo.dto.response;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuggestedProductDTO {
    private long id;
    private String name;
    private double unitPrice;
    private String image;
    private String link;
}
