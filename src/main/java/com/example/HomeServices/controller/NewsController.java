package com.example.HomeServices.controller;

import com.example.HomeServices.dto.CreateNewsDto;
import com.example.HomeServices.dto.NewsDto;
import com.example.HomeServices.dto.NewsShortDto;
import com.example.HomeServices.dto.LikeRequestDTO;
import com.example.HomeServices.dto.LikeResponseDTO;
import com.example.HomeServices.service.NewsService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
@Tag(name = "Новости", description = "API для управления новостями")
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/latest")
    @Operation(summary = "Получить последние новости (старый метод)")
    public ResponseEntity<List<NewsShortDto>> getLatestNews(
            @Parameter(description = "Количество новостей", example = "2")
            @RequestParam(defaultValue = "2") int count) {
        List<NewsShortDto> news = newsService.getLatestNews(count);
        return ResponseEntity.ok(news);
    }

    @GetMapping
    @Operation(summary = "Получить все новости с пагинацией и лайками")
    public ResponseEntity<Map<String, Object>> getAllNews(
            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Поле для сортировки", example = "createdAt")
            @RequestParam(defaultValue = "createdAt") String sortBy,

            @Parameter(description = "Направление сортировки", example = "DESC")
            @RequestParam(defaultValue = "DESC") String direction,

            @Parameter(description = "ID пользователя для проверки лайков", example = "1")
            @RequestParam(defaultValue = "1") Long userId) {

        Sort sort = direction.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<NewsShortDto> newsPage = newsService.getAllNewsWithLikes(pageable, userId);

        Map<String, Object> response = new HashMap<>();
        response.put("content", newsPage.getContent());
        response.put("currentPage", newsPage.getNumber());
        response.put("totalItems", newsPage.getTotalElements());
        response.put("totalPages", newsPage.getTotalPages());
        response.put("pageSize", newsPage.getSize());
        response.put("hasNext", newsPage.hasNext());
        response.put("hasPrevious", newsPage.hasPrevious());
        response.put("isFirst", newsPage.isFirst());
        response.put("isLast", newsPage.isLast());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить новость по ID (старый метод)")
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
    @Operation(summary = "Поставить лайк новости (старый метод)")
    public ResponseEntity<NewsDto> likeNews(
            @Parameter(description = "ID новости", example = "1")
            @PathVariable Long id) {
        NewsDto liked = newsService.likeNews(id);
        return ResponseEntity.ok(liked);
    }

    @GetMapping("/search")
    @Operation(summary = "Поиск новостей с лайками")
    public ResponseEntity<Map<String, Object>> searchNews(
            @Parameter(description = "Поисковый запрос", example = "новый год")
            @RequestParam String query,

            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "ID пользователя для проверки лайков", example = "1")
            @RequestParam(defaultValue = "1") Long userId) {

        Pageable pageable = PageRequest.of(page, size);
        Page<NewsShortDto> resultsPage = newsService.searchNewsWithLikes(query, pageable, userId);

        Map<String, Object> response = new HashMap<>();
        response.put("content", resultsPage.getContent());
        response.put("currentPage", resultsPage.getNumber());
        response.put("totalItems", resultsPage.getTotalElements());
        response.put("totalPages", resultsPage.getTotalPages());
        response.put("pageSize", resultsPage.getSize());
        response.put("hasNext", resultsPage.hasNext());
        response.put("hasPrevious", resultsPage.hasPrevious());

        return ResponseEntity.ok(response);
    }

    // ========== НОВЫЕ ЭНДПОИНТЫ ДЛЯ ЛАЙКОВ ==========

    @GetMapping("/latest-with-likes")
    @Operation(summary = "Получить последние новости с информацией о лайках пользователя")
    public ResponseEntity<List<NewsShortDto>> getLatestNewsWithLikes(
            @Parameter(description = "Количество новостей", example = "6")
            @RequestParam(defaultValue = "6") int count,

            @Parameter(description = "ID пользователя для проверки лайков", example = "1")
            @RequestParam(defaultValue = "1") Long userId) {

        List<NewsShortDto> news = newsService.getLatestNewsWithLikes(count, userId);
        return ResponseEntity.ok(news);
    }

    @GetMapping("/{id}/with-likes")
    @Operation(summary = "Получить новость по ID с информацией о лайках пользователя")
    public ResponseEntity<NewsDto> getNewsByIdWithLikes(
            @Parameter(description = "ID новости", example = "1")
            @PathVariable Long id,

            @Parameter(description = "ID пользователя для проверки лайков", example = "1")
            @RequestParam(defaultValue = "1") Long userId) {

        NewsDto news = newsService.getNewsByIdWithLike(id, userId);
        return ResponseEntity.ok(news);
    }

    @PostMapping("/{id}/toggle-like")
    @Operation(summary = "Поставить/убрать лайк новости")
    public ResponseEntity<LikeResponseDTO> toggleLike(
            @Parameter(description = "ID новости", example = "1")
            @PathVariable Long id,

            @RequestBody LikeRequestDTO likeRequest) {

        LikeResponseDTO response = newsService.toggleLike(id, likeRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/like-status/{userId}")
    @Operation(summary = "Проверить, поставил ли пользователь лайк новости")
    public ResponseEntity<LikeResponseDTO> getLikeStatus(
            @Parameter(description = "ID новости", example = "1")
            @PathVariable Long id,

            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable Long userId) {

        LikeResponseDTO response = newsService.getLikeStatus(id, userId);
        return ResponseEntity.ok(response);
    }


}