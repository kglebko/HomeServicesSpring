package com.example.HomeServices.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/images")
@CrossOrigin("*")
public class ImageController {

    @GetMapping("/{filename:.+}")
    public ResponseEntity<ByteArrayResource> getImage(@PathVariable String filename) {
        System.out.println("========================================");
        System.out.println("🖼️ ЗАПРОС ИЗОБРАЖЕНИЯ: " + filename);

        try {
            // Абсолютный путь к вашей папке
            String absolutePath = "D:\\4 курс\\Curs4\\news_photos\\" + filename;
            System.out.println("📁 Путь к файлу: " + absolutePath);

            Path path = Paths.get(absolutePath);

            if (Files.exists(path) && Files.isRegularFile(path)) {
                byte[] imageData = Files.readAllBytes(path);
                System.out.println("✅ Файл найден, размер: " + imageData.length + " байт");

                ByteArrayResource resource = new ByteArrayResource(imageData);

                // Определяем Content-Type
                MediaType mediaType = MediaType.IMAGE_PNG;
                if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg")) {
                    mediaType = MediaType.IMAGE_JPEG;
                }

                System.out.println("✅ Отправляем изображение");
                System.out.println("========================================");

                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .contentLength(imageData.length)
                        .body(resource);
            } else {
                System.out.println("❌ Файл не найден по пути: " + absolutePath);
                System.out.println("========================================");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (IOException e) {
            System.out.println("❌ Ошибка чтения файла: " + e.getMessage());
            System.out.println("========================================");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            System.out.println("❌ Неожиданная ошибка: " + e.getMessage());
            e.printStackTrace();
            System.out.println("========================================");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}