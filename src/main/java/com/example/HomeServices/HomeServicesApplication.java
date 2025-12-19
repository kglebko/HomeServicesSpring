package com.example.HomeServices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.annotation.PostConstruct;
import java.io.File;

@SpringBootApplication
public class HomeServicesApplication {

    @PostConstruct
    public void init() {
        // Выводим информацию о путях
        System.out.println("\n📁 ===== НАСТРОЙКА ПУТЕЙ ===== 📁");

        // Текущая рабочая директория
        File currentDir = new File(".");
        System.out.println("📁 Текущая рабочая директория: " + currentDir.getAbsolutePath());

        // Проверяем разные возможные пути
        String[] possiblePaths = {
                "./uploads/news_photos/",
                "uploads/news_photos/",
                "src/main/resources/static/images/",
                "src/main/resources/static/",
                "static/images/"
        };

        for (String path : possiblePaths) {
            File dir = new File(path);
            System.out.println("\n🔍 Проверяем путь: " + path);
            System.out.println("   Абсолютный путь: " + dir.getAbsolutePath());
            System.out.println("   Существует: " + dir.exists());
            System.out.println("   Это директория: " + (dir.exists() && dir.isDirectory()));

            if (dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles();
                if (files != null && files.length > 0) {
                    System.out.println("   Файлы в папке:");
                    for (int i = 0; i < Math.min(files.length, 5); i++) {
                        System.out.println("   - " + files[i].getName());
                    }
                } else {
                    System.out.println("   Папка пустая");
                }
            }
        }

        System.out.println("\n🌐 URL для тестирования изображений:");
        System.out.println("   http://localhost:8080/images/news1.png");
        System.out.println("   http://192.168.0.104:8080/images/news1.png");
        System.out.println("========================================\n");
    }

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