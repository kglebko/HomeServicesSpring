package com.example.HomeServices.dto;

import lombok.Data;

@Data
public class MeterReadingRequest {
    private Long userId;
    private Integer meter1;
    private Integer meter2;
    private Integer meter3;
    private Integer meter4;
}