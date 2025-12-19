package com.example.HomeServices.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillResponseDto {
    private Long id;
    private Long userId;
    private LocalDate period;
    private BigDecimal accruedAmount;
    private LocalDate accruedDate;
    private BigDecimal meterSum;
    private String status;

}