package com.apinayami.demo.repository;


import com.apinayami.demo.model.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IImageRepository extends JpaRepository<ImageModel, Long> {
    ImageModel findByUrlAndProductModelId(String url, long id);
}
