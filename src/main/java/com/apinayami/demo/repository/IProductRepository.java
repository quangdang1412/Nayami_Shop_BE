package com.apinayami.demo.repository;

import com.apinayami.demo.model.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<ProductModel, Long>, JpaSpecificationExecutor<ProductModel> {
    @Query("SELECT p FROM ProductModel p JOIN SerialProductModel sp ON sp.productModel = p WHERE sp.active = true GROUP BY p HAVING COUNT(sp) <= 6")
    List<ProductModel> getProductOutOfStock();

    @Query("SELECT COUNT(sp) FROM SerialProductModel sp WHERE sp.active = true AND sp.productModel.id = :id")
    Integer getQuantityProductInStock(@Param("id") long id);

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
