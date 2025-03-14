package com.apinayami.demo.repository;

import com.apinayami.demo.model.CartItemModel;
import com.apinayami.demo.model.ProductModel;
import com.apinayami.demo.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICartItemRepository extends JpaRepository<CartItemModel, Long> {
    List<CartItemModel> findByCustomerModel(UserModel customerModel);
    Optional<CartItemModel> findByCustomerModelAndProductModel(UserModel customerModel, ProductModel productModel);
    void deleteByCustomerModel(UserModel customerModel);
    Optional<CartItemModel> findById(Long id);

}