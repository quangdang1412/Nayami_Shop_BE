package com.apinayami.demo.repository;

import com.apinayami.demo.model.ConfigurationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ConfigurationRepository extends JpaRepository<ConfigurationModel,Long> {
}
