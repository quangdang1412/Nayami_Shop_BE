package com.apinayami.demo.repository;

import com.apinayami.demo.model.OtherConfigurationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface IOtherConfigurationRepository extends JpaRepository<OtherConfigurationModel, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM OtherConfigurationModel o WHERE o.configurationModel.id = :id")
    void deleteByConfigurationModel(long id);

}
