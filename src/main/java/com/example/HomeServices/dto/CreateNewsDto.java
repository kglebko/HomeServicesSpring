package com.example.HomeServices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateNewsDto {
    private String title;
    private String content;
    private String fullContent;
    private String category;
    private String imageUrl;
    private String author;
}