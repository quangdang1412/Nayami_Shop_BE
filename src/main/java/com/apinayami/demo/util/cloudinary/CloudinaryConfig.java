package com.apinayami.demo.util.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class CloudinaryConfig {

    @Value("${CLOUD_NAME}")
    private String name;
    @Value("${CLOUD_SECRET}")
    private String secret;
    @Value("${CLOUD_KEY}")
    private String key;

    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", name,
                "api_key", key,
                "api_secret", secret
        ));
    };
}
