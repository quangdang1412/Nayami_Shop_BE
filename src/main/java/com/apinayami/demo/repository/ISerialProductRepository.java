package com.apinayami.demo.repository;

import com.apinayami.demo.model.SerialProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISerialProductRepository extends JpaRepository<SerialProductModel, Long> {

    List<SerialProductModel> findSerialProductModelByProductModel_Id(long id);
}
