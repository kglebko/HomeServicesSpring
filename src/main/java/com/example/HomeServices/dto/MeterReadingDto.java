package com.example.HomeServices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class MeterReadingDto {
    private Long id;
    private LocalDate readingMonth;
    private Integer meter1;
    private Integer meter2;
    private Integer meter3;
    private Integer meter4;
}
