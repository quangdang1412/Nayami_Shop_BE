package com.apinayami.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Objects;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = { "categoryModel", "otherConfigurationList" })
public class ConfigurationModel extends AbstractEntity<Long> {

    // reference
    @OneToOne
    @JoinColumn(name = "category_id")
    private CategoryModel categoryModel;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "configurationModel", cascade = CascadeType.ALL)
    private List<OtherConfiguration> otherConfigurationList;

}
