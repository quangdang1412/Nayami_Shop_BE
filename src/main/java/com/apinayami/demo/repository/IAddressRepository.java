package com.apinayami.demo.repository;

import com.apinayami.demo.model.AddressModel;
import com.apinayami.demo.model.UserModel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAddressRepository extends JpaRepository<AddressModel, Long> {
    List<AddressModel> findByCustomerModel(UserModel customerModel);
    List<AddressModel> findByCustomerModel_IdAndActiveTrue(Long customerId);


}