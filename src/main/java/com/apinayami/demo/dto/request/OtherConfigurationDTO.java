package com.apinayami.demo.dto.request;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtherConfigurationDTO implements Serializable {
    private long id;
    private String name;
    private String value;
}
