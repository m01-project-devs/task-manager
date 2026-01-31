package com.m01project.taskmanager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    // The value will be a comma-separated string, which Spring automatically converts to a String array.
    @Value("${app.cors.allowed-origins}")
    private String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Apply this CORS configuration to all API paths
                .allowedOrigins(allowedOrigins) // Use the configurable list of origins
                .allowCredentials(false) // Authentication is via token in header, not cookies
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // Allow all necessary HTTP methods
                .allowedHeaders("Authorization", "Content-Type");
    }

}
