package com.apinayami.demo.repository;

import com.apinayami.demo.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<UserModel,Long> {
}
