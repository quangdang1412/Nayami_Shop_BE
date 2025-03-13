package com.apinayami.demo.service;

import com.apinayami.demo.model.ImageModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    List<ImageModel> getAllImage();

    ImageModel findImageByURL(String id);

    void addImage(String url);

    boolean isPresent(String url);

    String upload(MultipartFile multipartFile);
}
