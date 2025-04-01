package com.apinayami.demo.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResLoginDTO {
    private long id;
    private String accessToken;
    private String refreshToken;
}
