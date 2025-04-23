package com.apinayami.demo.service.Impl;

import com.apinayami.demo.model.ImageModel;
import com.apinayami.demo.model.ProductModel;
import com.apinayami.demo.repository.IImageRepository;
import com.apinayami.demo.service.IImageService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements IImageService {

    @Autowired
    private Cloudinary cloudinary;

    private final IImageRepository imageRepository;
    @Value("${app.firebase.bucket}")
    private String BUCKET_NAME;
    @Value("${app.firebase.file}")
    private String FIREBASE_PRIVATE_KEY;

    @Override
    public List<ImageModel> getAllImage() {
        return null;
    }

    @Override
    public ImageModel findImageByURLAndProductId(String url, long pid) {
        return imageRepository.findByUrlAndProductModelId(url, pid);
    }


    @Override
    public void addImage(String code, ProductModel productModel) {
        ImageModel imageModel = new ImageModel();
        imageModel.setUrl(code);
        imageModel.setProductModel(productModel);
        imageRepository.save(imageModel);
    }

    @Override
    public boolean isPresent(String url, long id) {
        return imageRepository.findByUrlAndProductModelId(url, id) != null;
    }


    public String uploadFile(File file, String fileName) throws IOException {
        if (FIREBASE_PRIVATE_KEY == null) {
            throw new RuntimeException("Firebase private key is not found. " +
                    "Please set 'app.firebase.file' in application.properties");
        }
        BlobId blobId = BlobId.of(BUCKET_NAME, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        try (InputStream inputStream = ImageServiceImpl.class.getClassLoader().getResourceAsStream(FIREBASE_PRIVATE_KEY)) {
            if (inputStream == null) {
                throw new RuntimeException("Firebase private key is not found. " +
                        "Please check 'app.firebase.file' in application.properties");
            }
            // change the file name with your one
            Credentials credentials = GoogleCredentials.fromStream(inputStream);
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
            storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        }
        String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/" + BUCKET_NAME + "/o/%s?alt=media";
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    public File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }
        return tempFile;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    @Override
    public String upload(MultipartFile multipartFile) {
        try {
            String fileName = multipartFile.getOriginalFilename();
            File file = this.convertToFile(multipartFile, fileName);
            String URL = this.uploadFile(file, fileName);
            if (file.delete()) {
                System.out.println("File deleted successfully");
            } else {
                System.err.println("Failed to delete file");
            }
            return URL;
        } catch (Exception e) {
            e.printStackTrace();
            return "Image couldn't upload, Something went wrong";
        }
    }

    @Override
    public boolean cloudinaryDelete(String url) {
        try {
            ImageModel imageModel = imageRepository.findByUrl(url);
            imageRepository.delete(imageModel);

            String publicId = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return "ok".equals(result.get("result"));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
