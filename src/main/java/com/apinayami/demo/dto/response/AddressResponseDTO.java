package com.apinayami.demo.dto.response;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseDTO {
    private String shippingContactNumber;
    private String detail;
    private String province;
    private String ward;
    private String district;
    private boolean active;
}