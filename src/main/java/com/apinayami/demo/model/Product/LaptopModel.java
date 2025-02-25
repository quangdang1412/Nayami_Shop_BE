package com.apinayami.demo.model.Product;

import com.apinayami.demo.model.ConfigurationLaptopModel;
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
public class LaptopModel extends BaseProduct {

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "configuration_id", nullable = false)
    private ConfigurationLaptopModel configurationLaptopModel;
}
