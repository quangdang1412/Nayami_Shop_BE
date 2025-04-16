package com.apinayami.demo.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResLoginDTO {
    public ResLoginDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
//    Test id user
    private long id;
    private String accessToken;
    private String refreshToken;
}
