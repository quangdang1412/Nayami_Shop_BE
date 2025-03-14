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
@EqualsAndHashCode(callSuper = true)
public class ShippingModel extends AbstractEntity<Long> {
    @ManyToOne()
    @JoinColumn(name = "address_id")
    private AddressModel shippingAddress;
    private double shippingFee;

}
