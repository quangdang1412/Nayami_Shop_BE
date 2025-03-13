package com.apinayami.demo.service;

import com.apinayami.demo.model.ImageModel;
import com.apinayami.demo.model.ProductModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    List<ImageModel> getAllImage();

    ImageModel findImageByURLAndProductId(String id, long pid);

    void addImage(String url, ProductModel productModel);

    boolean isPresent(String url, long pid);

    String upload(MultipartFile multipartFile);
}
