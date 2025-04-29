package com.apinayami.demo.repository;

import com.apinayami.demo.model.CartModel;
import com.apinayami.demo.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICartRepository extends JpaRepository<CartModel, Long> {
    Optional<CartModel> findByCustomerModel(UserModel userModel);
}