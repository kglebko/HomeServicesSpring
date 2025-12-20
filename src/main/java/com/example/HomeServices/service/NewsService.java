package com.example.HomeServices.service;

import com.example.HomeServices.dto.CreateNewsDto;
import com.example.HomeServices.dto.NewsDto;
import com.example.HomeServices.dto.NewsShortDto;
import com.example.HomeServices.entity.News;
import com.example.HomeServices.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsService {

    private final NewsRepository newsRepository;

    // ========== ПУБЛИЧНЫЕ МЕТОДЫ ==========

    public List<NewsShortDto> getLatestNews(int count) {
        log.info("📰 Получение последних {} новостей", count);
        List<News> newsList = newsRepository.findLatestNews(count);
        log.info("✅ Найдено {} новостей", newsList.size());

        return newsList.stream()
                .map(this::convertToShortDto)
                .collect(Collectors.toList());
    }

    public Page<NewsShortDto> getAllNews(Pageable pageable) {
        Page<News> newsPage = newsRepository.findByIsActiveTrueOrderByCreatedAtDesc(pageable);
        return newsPage.map(this::convertToShortDto);
    }

    public NewsDto getNewsById(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Новость с ID " + id + " не найдена"));
        incrementViews(news);
        return convertToDto(news);
    }

    @Transactional
    public NewsDto createNews(CreateNewsDto createDto) {
        log.info("Создание новости: {}", createDto.getTitle());

        News news = new News();
        news.setTitle(createDto.getTitle());
        news.setContent(createDto.getContent());
        news.setFullContent(createDto.getFullContent());
        news.setCategory(createDto.getCategory());
        news.setImageUrl(createDto.getImageUrl());
        news.setAuthor(createDto.getAuthor() != null ? createDto.getAuthor() : "Администрация");
        news.setViewCount(0);
        news.setLikesCount(0);
        news.setCommentsCount(0);
        news.setIsActive(true);

        News saved = newsRepository.save(news);
        log.info("✅ Создана новость с ID: {}", saved.getId());

        return convertToDto(saved);
    }

    @Transactional
    public NewsDto updateNews(Long id, CreateNewsDto createDto) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Новость с ID " + id + " не найдена"));

        news.setTitle(createDto.getTitle());
        news.setContent(createDto.getContent());
        news.setFullContent(createDto.getFullContent());
        news.setCategory(createDto.getCategory());
        news.setImageUrl(createDto.getImageUrl());
        if (createDto.getAuthor() != null) {
            news.setAuthor(createDto.getAuthor());
        }

        News updated = newsRepository.save(news);
        log.info("✅ Обновлена новость с ID: {}", id);

        return convertToDto(updated);
    }

    @Transactional
    public void deleteNews(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Новость с ID " + id + " не найдена"));
        news.setIsActive(false);
        newsRepository.save(news);
        log.info("✅ Новость с ID {} деактивирована", id);
    }

    @Transactional
    public NewsDto likeNews(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Новость с ID " + id + " не найдена"));
        news.setLikesCount((news.getLikesCount() != null ? news.getLikesCount() : 0) + 1);
        News liked = newsRepository.save(news);
        return convertToDto(liked);
    }

    public Page<NewsShortDto> searchNews(String query, Pageable pageable) {
        return newsRepository.searchNews(query, pageable)
                .map(this::convertToShortDto);
    }

    @Transactional
    public void incrementViews(News news) {
        news.setViewCount((news.getViewCount() != null ? news.getViewCount() : 0) + 1);
        newsRepository.save(news);
        log.debug("📈 Увеличены просмотры для новости ID: {}", news.getId());
    }

    // ========== ПРИВАТНЫЕ МЕТОДЫ ==========

    private NewsShortDto convertToShortDto(News news) {
        NewsShortDto dto = new NewsShortDto();
        dto.setId(news.getId());
        dto.setTitle(news.getTitle());
        dto.setContent(news.getContent());
        dto.setCategory(news.getCategory() != null ? news.getCategory() : "Общие");

        // Просто возвращаем относительный путь
        String imageUrl = news.getImageUrl();
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            dto.setImageUrl("/uploads/" + imageUrl);
        } else {
            dto.setImageUrl(null);
        }

        dto.setCreatedAt(news.getCreatedAt());
        dto.setTimeAgo(calculateTimeAgo(news.getCreatedAt()));
        dto.setLikesCount(news.getLikesCount() != null ? news.getLikesCount() : 0);
        dto.setCommentsCount(news.getCommentsCount() != null ? news.getCommentsCount() : 0);
        dto.setViewCount(news.getViewCount() != null ? news.getViewCount() : 0);

        return dto;
    }

    private NewsDto convertToDto(News news) {
        NewsDto dto = new NewsDto();
        dto.setId(news.getId());
        dto.setTitle(news.getTitle());
        dto.setContent(news.getContent());
        dto.setFullContent(news.getFullContent());
        dto.setCategory(news.getCategory() != null ? news.getCategory() : "Общие");

        // Та же логика
        String imageUrl = news.getImageUrl();
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            dto.setImageUrl("/uploads/" + imageUrl);
        } else {
            dto.setImageUrl(null);
        }

        dto.setAuthor(news.getAuthor() != null ? news.getAuthor() : "Администрация");
        dto.setViewCount(news.getViewCount() != null ? news.getViewCount() : 0);
        dto.setLikesCount(news.getLikesCount() != null ? news.getLikesCount() : 0);
        dto.setCommentsCount(news.getCommentsCount() != null ? news.getCommentsCount() : 0);
        dto.setIsActive(news.getIsActive() != null ? news.getIsActive() : true);
        dto.setCreatedAt(news.getCreatedAt());
        dto.setUpdatedAt(news.getUpdatedAt());
        dto.setTimeAgo(calculateTimeAgo(news.getCreatedAt()));

        return dto;
    }

    private String calculateTimeAgo(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "недавно";
        }

        LocalDateTime now = LocalDateTime.now();
        long hours = java.time.Duration.between(dateTime, now).toHours();

        if (hours < 1) {
            long minutes = java.time.Duration.between(dateTime, now).toMinutes();
            if (minutes < 1) {
                return "только что";
            }
            return minutes + " мин. назад";
        } else if (hours < 24) {
            return hours + " час. назад";
        } else {
            long days = hours / 24;
            return days + " дн. назад";
        }
    }
}