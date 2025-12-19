// src/main/java/com/example/HomeServices/service/NewsService.java
package com.example.HomeServices.service;

import com.example.HomeServices.dto.CreateNewsDto;
import com.example.HomeServices.dto.NewsDto;
import com.example.HomeServices.dto.NewsShortDto;
import com.example.HomeServices.entity.News;
import com.example.HomeServices.repository.NewsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    // Получить последние новости (для главной страницы)
    public List<NewsShortDto> getLatestNews(int count) {
        return newsRepository.findLatestNews(count).stream()
                .map(this::convertToShortDto)
                .collect(Collectors.toList());
    }

    // Получить все новости с пагинацией
    public Page<NewsShortDto> getAllNews(Pageable pageable) {
        return newsRepository.findByIsActiveTrueOrderByCreatedAtDesc(pageable)
                .map(this::convertToShortDto);
    }

    // Получить новость по ID
    @Transactional
    public NewsDto getNewsById(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Новость не найдена с id: " + id));

        // Увеличиваем счетчик просмотров
        news.setViewCount(news.getViewCount() + 1);
        newsRepository.save(news);

        return convertToDto(news);
    }

    // Создать новость
    @Transactional
    public NewsDto createNews(CreateNewsDto dto) {
        News news = new News();
        news.setTitle(dto.getTitle());
        news.setContent(dto.getContent());
        news.setFullContent(dto.getFullContent());
        news.setCategory(dto.getCategory());
        news.setImageUrl(dto.getImageUrl());
        news.setAuthor(dto.getAuthor());

        News saved = newsRepository.save(news);
        return convertToDto(saved);
    }

    // Обновить новость
    @Transactional
    public NewsDto updateNews(Long id, CreateNewsDto dto) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Новость не найдена с id: " + id));

        news.setTitle(dto.getTitle());
        news.setContent(dto.getContent());
        news.setFullContent(dto.getFullContent());
        news.setCategory(dto.getCategory());
        news.setImageUrl(dto.getImageUrl());
        news.setAuthor(dto.getAuthor());

        News updated = newsRepository.save(news);
        return convertToDto(updated);
    }

    // Мягкое удаление
    @Transactional
    public void deleteNews(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Новость не найдена с id: " + id));
        news.setIsActive(false);
        newsRepository.save(news);
    }

    // Поставить лайк
    @Transactional
    public NewsDto likeNews(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Новость не найдена с id: " + id));
        news.setLikesCount(news.getLikesCount() + 1);
        News updated = newsRepository.save(news);
        return convertToDto(updated);
    }

    // Поиск новостей
    public Page<NewsShortDto> searchNews(String query, Pageable pageable) {
        return newsRepository.searchNews(query, pageable)
                .map(this::convertToShortDto);
    }

    // Конвертеры
    private NewsShortDto convertToShortDto(News news) {
        NewsShortDto dto = new NewsShortDto();
        dto.setId(news.getId());
        dto.setTitle(news.getTitle());
        dto.setContent(news.getContent());
        dto.setCategory(news.getCategory());
        dto.setImageUrl(news.getImageUrl());
        dto.setLikesCount(news.getLikesCount());
        dto.setCommentsCount(news.getCommentsCount());
        dto.setViewCount(news.getViewCount());
        dto.setCreatedAt(news.getCreatedAt());
        dto.setTimeAgo(calculateTimeAgo(news.getCreatedAt()));
        return dto;
    }

    private NewsDto convertToDto(News news) {
        NewsDto dto = new NewsDto();
        dto.setId(news.getId());
        dto.setTitle(news.getTitle());
        dto.setContent(news.getContent());
        dto.setFullContent(news.getFullContent());
        dto.setCategory(news.getCategory());
        dto.setImageUrl(news.getImageUrl());
        dto.setAuthor(news.getAuthor());
        dto.setCreatedAt(news.getCreatedAt());
        dto.setUpdatedAt(news.getUpdatedAt());
        dto.setViewCount(news.getViewCount());
        dto.setLikesCount(news.getLikesCount());
        dto.setCommentsCount(news.getCommentsCount());
        dto.setIsActive(news.getIsActive());
        dto.setTimeAgo(calculateTimeAgo(news.getCreatedAt()));
        return dto;
    }

    // Вспомогательный метод для форматирования времени
    private String calculateTimeAgo(LocalDateTime dateTime) {
        if (dateTime == null) return "";

        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(dateTime, now);

        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();

        if (minutes < 1) {
            return "Только что";
        } else if (minutes < 60) {
            return minutes + " мин. назад";
        } else if (hours < 24) {
            return hours + " час. назад";
        } else if (days == 1) {
            return "Вчера " + dateTime.toLocalTime().getHour() + ":" +
                    String.format("%02d", dateTime.toLocalTime().getMinute());
        } else if (days < 7) {
            return days + " дня назад";
        } else if (days < 30) {
            long weeks = days / 7;
            return weeks + " недели назад";
        } else {
            return dateTime.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        }
    }
}