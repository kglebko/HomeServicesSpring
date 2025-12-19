package com.example.HomeServices.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentHistoryDto(
        LocalDate period,
        String status,
        BigDecimal accruedAmount,
        LocalDate accruedDate,
        BigDecimal paidAmount,
        LocalDate paidDate
) {}
