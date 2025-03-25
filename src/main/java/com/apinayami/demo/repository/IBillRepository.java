package com.apinayami.demo.repository;

import com.apinayami.demo.model.BillModel;
import com.apinayami.demo.model.UserModel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IBillRepository extends JpaRepository<BillModel, Long> {
    List<BillModel> findByCustomerModelOrderByCreatedAtDesc(UserModel user);

    BillModel findByIdAndCustomerModel(Long billId, UserModel user);
}