package com.example.HomeServices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HomeServicesApplication {
    public static void main(String[] args) {
        // Жестко задаем параметры
        System.setProperty("spring.datasource.url",
                "jdbc:mysql://25.21.56.83:3306/home_services?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        System.setProperty("spring.datasource.username", "root");
        System.setProperty("spring.datasource.password", "ilyushin1718A");
        System.setProperty("spring.jpa.hibernate.ddl-auto", "update");

        SpringApplication.run(HomeServicesApplication.class, args);
    }
}