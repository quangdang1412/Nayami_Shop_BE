package com.apinayami.demo.model.Product;

import com.apinayami.demo.util.Enum.EHeadPhoneType;
import jakarta.persistence.Entity;
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
public class HeadPhoneModel extends BaseProduct {
    private EHeadPhoneType headPhoneType;
    private String color;
    private boolean mic;
    private boolean wired;
    private String technology;
    private Double batteryLife;
    private String otherFeature;
}
