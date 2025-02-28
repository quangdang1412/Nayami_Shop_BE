package com.apinayami.demo.repository;

import com.apinayami.demo.model.AdminModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAdminRepository extends JpaRepository<AdminModel, Long> {
}
