package com.apinayami.demo.model.Product;

import com.apinayami.demo.model.ConfigurationPhoneModel;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
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
public class PhoneModel extends BaseProduct {

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "configuration_id", nullable = true) // nullable true for test
    private ConfigurationPhoneModel configurationPhoneModel;
    private String color;

}
