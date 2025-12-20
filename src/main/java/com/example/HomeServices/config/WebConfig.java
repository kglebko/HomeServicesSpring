package com.example.HomeServices.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.annotation.PostConstruct;
import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostConstruct
    public void init() {
        File dir = new File(uploadDir);
        System.out.println("📁 Папка для изображений: " + dir.getAbsolutePath());
        System.out.println("📁 Существует: " + dir.exists());

        if (!dir.exists()) {
            dir.mkdirs();
            System.out.println("✅ Создана папка");
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        File dir = new File(uploadDir);
        String absolutePath = dir.getAbsolutePath();
        String resourceLocation = "file:" + absolutePath.replace("\\", "/") + "/";

        System.out.println("🌐 Регистрируем доступ к файлам:");
        System.out.println("   Папка: " + absolutePath);
        System.out.println("   URL: http://localhost:8080/uploads/");

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(resourceLocation);
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