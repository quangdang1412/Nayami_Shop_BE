package com.apinayami.demo.repository;

import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.util.Enum.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IUserRepository extends JpaRepository<UserModel,Long> {
    List<UserModel> findByType(Role role);
    UserModel findById(long id);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
}
