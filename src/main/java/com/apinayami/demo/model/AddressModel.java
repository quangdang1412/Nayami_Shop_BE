package com.apinayami.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressModel extends AbstractEntity<Long> {
    @ManyToOne()
    @JoinColumn(name = "customer_id")
    private CustomerModel customerModel;
    @Column
    private String shippingContactNumber;
    @Column
    private String detail;
    @Column
    private String province;
    @Column
    private String village;
    @Column
    private String district;
    @Column
    private boolean active;
}
