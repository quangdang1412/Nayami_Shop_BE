package com.apinayami.demo.model;

import com.apinayami.demo.model.Product.LaptopModel;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ConfigurationLaptopModel extends ConfigurationModel {
    private String webCamera;

    @OneToOne(mappedBy = "configurationLaptopModel", fetch = FetchType.LAZY)
    private LaptopModel laptopModel;
}
