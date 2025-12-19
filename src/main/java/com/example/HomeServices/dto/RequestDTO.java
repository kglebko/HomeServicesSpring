package com.example.HomeServices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {

    private Long id;
    private String serviceName;
    private String status;
    private String scheduledPeriod;
    private String actualTime;
    private String estimatedPrice;
    private String paidPrice;
    private String comment;
}
