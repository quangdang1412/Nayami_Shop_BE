package com.apinayami.demo.repository;

import com.apinayami.demo.model.BillModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBillRepository extends JpaRepository<BillModel, Long> {
}