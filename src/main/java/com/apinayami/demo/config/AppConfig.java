package com.apinayami.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOrigins(
                        "https://nayami-shop-fe.vercel.app",  // FE production domain
                        "http://localhost:5173"                // FE development domain
                )
                .allowedMethods("*")
                .allowedHeaders("*");
    }
}
