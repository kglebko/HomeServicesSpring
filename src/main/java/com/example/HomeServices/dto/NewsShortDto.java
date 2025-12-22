// src/main/java/com/example/HomeServices/dto/NewsShortDto.java
package com.example.HomeServices.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsShortDto {
    private Long id;
    private String title;
    private String content;
    private String category;
    private String imageUrl;
    private String timeAgo;
    private Integer likesCount;
    private Integer commentsCount;
    private Integer viewCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}