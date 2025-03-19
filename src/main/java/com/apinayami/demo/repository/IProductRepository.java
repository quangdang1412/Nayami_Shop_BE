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

    @Query("from ProductModel p where p.discountDetailModel IS NOT NULL")
    List<ProductModel> getProductModelsHaveDiscountModel();

//    @Query("SELECT p FROM ProductModel p WHERE p.displayStatus = true AND (:categoryID IS NULL OR lower(p.categoryModel.id) LIKE lower(:categoryID)) AND (:brandID IS NULL OR lower(p.brandModel.id) LIKE lower(:brandID)) AND (:searchQuery IS NULL OR lower(p.productName) LIKE lower(:searchQuery))")
//    Page<ProductModel> getProductForPage(
//            @Param("categoryID") String categoryID,
//            @Param("brandID") String brandID,
//            @Param("searchQuery") String searchQuery,
//            Pageable pageable
//    );

    @Query("SELECT COUNT(d) FROM ProductModel d WHERE d.ratingAvg = :star")
    Integer getQuantityProductOfRating(Integer star);
}
