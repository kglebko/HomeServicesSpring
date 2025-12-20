package com.example.HomeServices.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir:./uploads/news_photos}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Преобразуем путь для доступа к файлам
        String absolutePath = Paths.get(uploadDir).toAbsolutePath().toString();

        // Для Windows: заменяем обратные слеши
        String fileUrl = "file:" + absolutePath.replace("\\", "/") + "/";

        // Настраиваем доступ к файлам через URL /uploads/**
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(fileUrl);

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600);
    }
}