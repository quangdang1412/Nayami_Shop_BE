package com.apinayami.demo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Nayami Shop API",
                version = "1.0",
                description = "API documentation for Nayami Shop"
        )
)
public class OpenAPIConfig {
}
