package com.apinayami.demo.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"categoryModel", "otherConfigurationModelList"})
public class ConfigurationModel extends AbstractEntity<Long> {


    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryModel categoryModel;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "configurationModel", cascade = CascadeType.ALL)
    private List<OtherConfigurationModel> otherConfigurationModelList;

}
