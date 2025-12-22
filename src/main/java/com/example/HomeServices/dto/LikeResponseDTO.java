package com.example.HomeServices.dto;

import lombok.Data;

@Data
public class LikeResponseDTO {
    private boolean success;
    private boolean liked;
    private int likesCount;
    private String message;
}