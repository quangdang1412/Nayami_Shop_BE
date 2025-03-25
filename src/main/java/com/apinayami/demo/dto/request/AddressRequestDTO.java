package com.apinayami.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequestDTO {
    private String province;
    private String district;
    private String ward;
    private String phone;
    private String addressName;
    private String recipientName;
    private Long userId;
}
