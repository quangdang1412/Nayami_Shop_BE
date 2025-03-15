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
public class AddressModel extends AbstractEntity<Long> {
    private String shippingContactNumber;
    private String detail;
    private String province;
    private String ward;
    private String district;
    private boolean active;

    //reference
    @ManyToOne()
    @JoinColumn(name = "customer_id")
    private UserModel customerModel;
}
