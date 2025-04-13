package com.apinayami.demo.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResLoginDTO {
//    Test id user
    private long id;
    private String accessToken;
    private String refreshToken;
}
