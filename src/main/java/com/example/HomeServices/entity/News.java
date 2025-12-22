// src/main/java/com/example/HomeServices/entity/News.java
package com.example.HomeServices.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "news")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "full_content", columnDefinition = "TEXT")
    private String fullContent;

    @Column(nullable = false, length = 100)
    private String category;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(nullable = false, length = 100)
    private String author;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Column(name = "likes_count")
    private Integer likesCount = 0;

    // ДОБАВИЛИ ЭТО ПОЛЕ
    @Column(name = "liked_by", columnDefinition = "JSON")
    private String likedBy = "[]";

    @Column(name = "comments_count")
    private Integer commentsCount = 0;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Transient
    @JsonProperty("isLiked")
    private Boolean isLiked = false;
}