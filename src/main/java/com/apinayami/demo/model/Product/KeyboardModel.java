package com.apinayami.demo.model.Product;

import com.apinayami.demo.util.Enum.ELayoutType;
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
public class KeyboardModel extends BaseProduct {
    private String color;
    private String led;
    private Double batteryLife;
    private boolean wired;
    private ELayoutType layoutTyp;
}
