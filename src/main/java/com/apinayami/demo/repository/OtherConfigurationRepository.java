package com.apinayami.demo.repository;

import com.apinayami.demo.model.OtherConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtherConfigurationRepository extends JpaRepository<OtherConfiguration,Long> {
}
