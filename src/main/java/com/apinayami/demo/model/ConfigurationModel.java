package com.apinayami.demo.model;

import com.apinayami.demo.util.Enum.ChargerType;
import com.apinayami.demo.util.Enum.EScreenPanel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ConfigurationModel extends AbstractEntity<Long> {
    private Integer ram;
    private Integer rom;
    private Integer refreshRate;
    private Double sizeScreen;
    private Double weight;
    private Integer battery;
    private String cpu;
    private String resolution;
    private String os;
    private ChargerType chargeType;
    private EScreenPanel screenPanel;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "configurationModel", cascade = CascadeType.ALL)
    private List<OtherConfiguration> otherConfigurationList;
}
