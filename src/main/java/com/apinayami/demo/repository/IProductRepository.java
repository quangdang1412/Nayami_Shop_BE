package com.apinayami.demo.repository;

import com.apinayami.demo.model.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends JpaRepository<ProductModel,Long> {
}
