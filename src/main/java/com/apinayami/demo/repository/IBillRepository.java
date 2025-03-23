package com.apinayami.demo.repository;

import com.apinayami.demo.model.BillModel;
import com.apinayami.demo.model.UserModel;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBillRepository extends JpaRepository<BillModel, Long> {
    Page<BillModel> findByCustomerModel(UserModel user, Pageable pageable);
}