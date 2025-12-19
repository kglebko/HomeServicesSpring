package com.example.HomeServices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {

    private Long id;
    private String serviceName;
    private String status;
    private String scheduledPeriod;
    private String actualTime;
    private BigDecimal estimatedPrice;
    private BigDecimal paidPrice;
    private String comment;
}
