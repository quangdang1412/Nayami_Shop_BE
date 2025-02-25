package com.apinayami.demo.model;

import com.apinayami.demo.model.Product.PhoneModel;
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
public class ConfigurationPhoneModel extends ConfigurationModel {
    private String frontCamera;
    private String backCamera;

    @OneToOne(mappedBy = "configurationPhoneModel", fetch = FetchType.LAZY)
    private PhoneModel phoneModel;
}
