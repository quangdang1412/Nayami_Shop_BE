package com.apinayami.demo.dto.response;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseDTO {
    private Long id;
    private String phone;
    private String recipientName;
    private String addressName;
    private String province;
    private String district;
    private String ward;
}