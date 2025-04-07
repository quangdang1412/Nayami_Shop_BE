package com.apinayami.demo.repository;

import com.apinayami.demo.model.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<CategoryModel, Long> {
    boolean existsByCategoryName(@Param("name") String name);
}
