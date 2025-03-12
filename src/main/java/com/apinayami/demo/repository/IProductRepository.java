package com.apinayami.demo.repository;

import com.apinayami.demo.model.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<ProductModel, Long> {
    @Query("from ProductModel p where p.quantity<=6")
    List<ProductModel> getProductOutOfStock();

    List<ProductModel> getProductModelsByCategoryModel_Id(long id);

    List<ProductModel> getProductModelsByBrandModelId(long id);
}
