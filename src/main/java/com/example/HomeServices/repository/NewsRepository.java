// src/main/java/com/example/HomeServices/repository/NewsRepository.java
package com.example.HomeServices.repository;

import com.example.HomeServices.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    List<News> findByIsActiveTrueOrderByCreatedAtDesc();

    Page<News> findByIsActiveTrueOrderByCreatedAtDesc(Pageable pageable);
    List<News> findByCategoryAndIsActiveTrueOrderByCreatedAtDesc(String category);

    @Query(value = "SELECT * FROM news WHERE is_active = true ORDER BY created_at DESC LIMIT :limit",
            nativeQuery = true)
    List<News> findLatestNews(@Param("limit") int limit);

    @Query("SELECT n FROM News n WHERE n.isActive = true AND " +
            "(LOWER(n.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(n.content) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "ORDER BY n.createdAt DESC")
    Page<News> searchNews(@Param("query") String query, Pageable pageable);

    // ДОБАВИЛИ ЭТОТ МЕТОД
    @Query("SELECT n FROM News n WHERE n.isActive = true ORDER BY n.createdAt DESC")
    List<News> findTop10ByIsActiveTrueOrderByCreatedAtDesc();
}