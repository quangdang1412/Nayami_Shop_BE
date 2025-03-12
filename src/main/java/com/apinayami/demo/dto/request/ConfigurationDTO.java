package com.apinayami.demo.dto.request;

import lombok.*;

import java.util.List;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigurationDTO {
    private long category;
    private List<OtherConfigurationDTO> listOtherConfigDTO;
}
