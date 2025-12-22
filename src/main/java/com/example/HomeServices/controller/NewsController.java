// src/main/java/com/example/HomeServices/controller/NewsController.java
package com.example.HomeServices.controller;

import com.example.HomeServices.dto.CreateNewsDto;
import com.example.HomeServices.dto.NewsDto;
import com.example.HomeServices.dto.NewsShortDto;
import com.example.HomeServices.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
@Tag(name = "Новости", description = "API для управления новостями")
@CrossOrigin(origins = "*")
public class NewsController {

    private final PaymentService.NewsService newsService;

    @GetMapping("/latest")
    @Operation(summary = "Получить последние новости",
            description = "Возвращает указанное количество последних новостей (для главной страницы)")
    public ResponseEntity<List<NewsShortDto>> getLatestNews(
            @Parameter(description = "Количество новостей", example = "2")
            @RequestParam(defaultValue = "2") int count) {
        List<NewsShortDto> news = newsService.getLatestNews(count);
        return ResponseEntity.ok(news);
    }

    @GetMapping
    @Operation(summary = "Получить все новости с пагинацией")
    public ResponseEntity<Page<NewsShortDto>> getAllNews(
            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Поле для сортировки", example = "createdAt")
            @RequestParam(defaultValue = "createdAt") String sortBy,

            @Parameter(description = "Направление сортировки", example = "DESC")
            @RequestParam(defaultValue = "DESC") String direction) {

        Sort sort = direction.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<NewsShortDto> news = newsService.getAllNews(pageable);

        return ResponseEntity.ok(news);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить новость по ID")
    public ResponseEntity<NewsDto> getNewsById(
            @Parameter(description = "ID новости", example = "1")
            @PathVariable Long id) {
        NewsDto news = newsService.getNewsById(id);
        return ResponseEntity.ok(news);
    }

    @PostMapping
    @Operation(summary = "Создать новую новость")
    public ResponseEntity<NewsDto> createNews(
            @RequestBody CreateNewsDto dto) {
        NewsDto created = newsService.createNews(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить новость")
    public ResponseEntity<NewsDto> updateNews(
            @Parameter(description = "ID новости", example = "1")
            @PathVariable Long id,

            @RequestBody CreateNewsDto dto) {
        NewsDto updated = newsService.updateNews(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить новость (мягкое удаление)")
    public ResponseEntity<Void> deleteNews(
            @Parameter(description = "ID новости", example = "1")
            @PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/like")
    @Operation(summary = "Поставить лайк новости")
    public ResponseEntity<NewsDto> likeNews(
            @Parameter(description = "ID новости", example = "1")
            @PathVariable Long id) {
        NewsDto liked = newsService.likeNews(id);
        return ResponseEntity.ok(liked);
    }

    @GetMapping("/search")
    @Operation(summary = "Поиск новостей")
    public ResponseEntity<Page<NewsShortDto>> searchNews(
            @Parameter(description = "Поисковый запрос", example = "новый год")
            @RequestParam String query,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<NewsShortDto> results = newsService.searchNews(query, pageable);

        return ResponseEntity.ok(results);
    }
}