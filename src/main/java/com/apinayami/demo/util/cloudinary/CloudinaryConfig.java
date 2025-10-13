package com.apinayami.demo.util.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Value("dwijkd4xi")
    private String name;
    @Value("862117432415768")
    private String secret;
    @Value("G5q1TlioVtStYHATJnPFokJkvIg")
    private String key;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", name,
                "api_key", key,
                "api_secret", secret
        ));
    }

    ;
}
