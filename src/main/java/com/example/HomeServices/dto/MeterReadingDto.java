package com.example.HomeServices.dto;

import java.time.LocalDate;

public record MeterReadingDto(
        Long id,
        LocalDate readingMonth,
        Integer meter1,
        Integer meter2,
        Integer meter3,
        Integer meter4
) {}
