package com.apinayami.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingModel extends AbstractEntity<Long>{
    @ManyToOne()
    @JoinColumn(name = "shipping_address_id")
    private AddressModel shippingAddress;
    @Column
    private double shippingFee;
}
