package com.apinayami.demo.repository;

import com.apinayami.demo.model.StaffModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IStaffRepository extends JpaRepository<StaffModel, Long> {
}
