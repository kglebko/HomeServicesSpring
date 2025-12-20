package com.example.HomeServices.service;

import com.example.HomeServices.dto.CreateNewsDto;
import com.example.HomeServices.dto.NewsDto;
import com.example.HomeServices.dto.NewsShortDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

    public List<NewsShortDto> getLatestNews(int count) {
        List<NewsShortDto> news = new ArrayList<>();

        for (int i = 1; i <= Math.min(count, 5); i++) {
            NewsShortDto dto = new NewsShortDto();
            dto.setId((long) i);
            dto.setTitle("Новость " + i);
            dto.setContent("Краткое содержание новости " + i);
            dto.setCategory("Общие");
            dto.setImageUrl("/images/news" + i + ".jpg");
            dto.setTimeAgo(i + " часа назад");
            dto.setLikesCount(i * 10);
            dto.setCommentsCount(i * 3);
            dto.setViewCount(i * 50);
            dto.setCreatedAt(LocalDateTime.now().minusHours(i));

            news.add(dto);
        }

        return news;
    }

    public Page<NewsShortDto> getAllNews(Pageable pageable) {
        // TODO: Реальная логика из БД с пагинацией
        List<NewsShortDto> news = new ArrayList<>();

        // Пример данных
        for (int i = 1; i <= 10; i++) {
            NewsShortDto dto = new NewsShortDto();
            dto.setId((long) i);
            dto.setTitle("Новость " + i);
            dto.setContent("Содержание " + i);
            dto.setCategory("Категория " + (i % 3 + 1));
            dto.setLikesCount(i * 15);
            dto.setViewCount(i * 75);
            dto.setCreatedAt(LocalDateTime.now().minusDays(i));
            news.add(dto);
        }

        return new PageImpl<>(news, pageable, news.size());
    }

    public NewsDto getNewsById(Long id) {
        NewsDto dto = new NewsDto();
        dto.setId(id);
        dto.setTitle("Полная новость " + id);
        dto.setContent("Краткое содержание");
        dto.setFullContent("Полное подробное содержание новости...");
        dto.setCategory("Образование");
        dto.setImageUrl("/images/full-news.jpg");
        dto.setAuthor("Администратор");
        dto.setViewCount(150);
        dto.setLikesCount(25);
        dto.setCommentsCount(8);
        dto.setIsActive(true);
        dto.setCreatedAt(LocalDateTime.now().minusDays(2));
        dto.setUpdatedAt(LocalDateTime.now().minusHours(3));
        dto.setTimeAgo("2 дня назад");

        return dto;
    }

    public NewsDto createNews(CreateNewsDto createDto) {
        NewsDto dto = new NewsDto();
        dto.setId(1L); // В реальности ID генерирует БД
        dto.setTitle(createDto.getTitle());
        dto.setContent(createDto.getContent());
        dto.setFullContent(createDto.getFullContent());
        dto.setCategory(createDto.getCategory());
        dto.setImageUrl(createDto.getImageUrl());
        dto.setAuthor(createDto.getAuthor());
        dto.setViewCount(0);
        dto.setLikesCount(0);
        dto.setCommentsCount(0);
        dto.setIsActive(true);
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
        dto.setTimeAgo("только что");

        return dto;
    }

    public NewsDto updateNews(Long id, CreateNewsDto createDto) {
        NewsDto dto = new NewsDto();
        dto.setId(id);
        dto.setTitle(createDto.getTitle());
        dto.setContent(createDto.getContent());
        dto.setFullContent(createDto.getFullContent());
        dto.setCategory(createDto.getCategory());
        dto.setImageUrl(createDto.getImageUrl());
        dto.setAuthor(createDto.getAuthor());
        dto.setViewCount(100); // существующие данные
        dto.setLikesCount(30); // существующие данные
        dto.setCommentsCount(5); // существующие данные
        dto.setIsActive(true);
        dto.setCreatedAt(LocalDateTime.now().minusDays(5));
        dto.setUpdatedAt(LocalDateTime.now());
        dto.setTimeAgo("обновлено только что");

        return dto;
    }

    public void deleteNews(Long id) {
        // В реальности: помечаем isActive = false
        System.out.println("Новость с ID " + id + " помечена как неактивная (мягкое удаление)");
    }

    public NewsDto likeNews(Long id) {
        NewsDto dto = new NewsDto();
        dto.setId(id);
        dto.setTitle("Новость с увеличенным лайком");
        dto.setLikesCount(101); // Увеличили на 1
        dto.setViewCount(155);
        dto.setUpdatedAt(LocalDateTime.now());

        return dto;
    }

    public Page<NewsShortDto> searchNews(String query, Pageable pageable) {
        List<NewsShortDto> results = new ArrayList<>();

        // Пример поиска
        if (query != null && !query.trim().isEmpty()) {
            for (int i = 1; i <= 3; i++) {
                NewsShortDto dto = new NewsShortDto();
                dto.setId((long) i);
                dto.setTitle("Результат поиска: " + query + " " + i);
                dto.setContent("Найдено по запросу: " + query);
                dto.setLikesCount(i * 5);
                dto.setViewCount(i * 20);
                dto.setCreatedAt(LocalDateTime.now().minusHours(i));
                results.add(dto);
            }
        }

        return new PageImpl<>(results, pageable, results.size());
    }
}