package com.example.HomeServices.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.math.BigDecimal;

@Data
public class CreateRequestDto {
    private Long serviceId;
    private LocalDate selectedDate;
    private LocalTime selectedStartTime;
    private LocalTime selectedEndTime;
    private String comment;
    private Long userId;
    private BigDecimal estimatedPrice;
}