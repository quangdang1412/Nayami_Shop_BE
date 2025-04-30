package com.apinayami.demo.repository;

import com.apinayami.demo.model.SerialProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISerialProductRepository extends JpaRepository<SerialProductModel, Long> {
}
