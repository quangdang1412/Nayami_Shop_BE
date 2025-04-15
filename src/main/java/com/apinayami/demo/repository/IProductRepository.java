package com.apinayami.demo.repository;

import com.apinayami.demo.model.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<ProductModel, Long>, JpaSpecificationExecutor<ProductModel> {
    @Query("from ProductModel p where p.quantity<=6")
    List<ProductModel> getProductOutOfStock();

    List<ProductModel> getProductModelsByCategoryModel_Id(long id);

    List<ProductModel> getProductModelsByBrandModelId(long id);

    @Query("from ProductModel p where p.discountDetailModel IS NOT NULL")
    List<ProductModel> getProductModelsHaveDiscountModel();

    @Query("SELECT COUNT(d) FROM ProductModel d WHERE d.ratingAvg = :star")
    Integer getQuantityProductOfRating(Integer star);

    @Query("SELECT COUNT(d) FROM ProductModel d")
    Long getQuantityOfProduct();

    List<ProductModel> findProductModelsByDisplayStatusIsTrue();
}
