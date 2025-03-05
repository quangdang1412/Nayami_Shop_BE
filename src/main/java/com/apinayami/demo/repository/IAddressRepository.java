package com.apinayami.demo.repository;

import com.apinayami.demo.model.AddressModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAddressRepository extends JpaRepository<AddressModel, Long> {
}