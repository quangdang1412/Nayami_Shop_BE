package com.apinayami.demo.repository;

import com.apinayami.demo.model.OtherConfigurationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOtherConfigurationRepository extends JpaRepository<OtherConfigurationModel, Long> {
}
