package com.apinayami.demo;

import org.springdoc.core.configuration.SpringDocSecurityConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

//@SpringBootApplication(exclude = SpringDocSecurityConfiguration.class)
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
