package com.apinayami.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
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
