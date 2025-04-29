package com.apinayami.demo.repository;

import com.apinayami.demo.model.CartItemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface ICartItemRepository extends JpaRepository<CartItemModel, Long> {
    Optional<CartItemModel> findById(Long id);

}