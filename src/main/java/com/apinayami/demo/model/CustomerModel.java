package com.apinayami.demo.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerModel extends UserModel {

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "customerModel", cascade = CascadeType.ALL)
    private Set<AddressModel> listAddress;
}
