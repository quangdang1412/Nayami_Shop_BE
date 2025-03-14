package com.apinayami.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@EqualsAndHashCode(callSuper = true, exclude = {"configurationModel"})
public class OtherConfigurationModel extends AbstractEntity<Long> {
    private String name;
    private String value;

    @ManyToOne
    @JoinColumn(name = "configuration_id")
    private ConfigurationModel configurationModel;
}
