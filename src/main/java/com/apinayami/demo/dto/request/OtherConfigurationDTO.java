package com.apinayami.demo.dto.request;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtherConfigurationDTO implements Serializable {
    private String name;
    private String value;
}
