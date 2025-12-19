package com.example.HomeServices.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "requests")
@Data
@NoArgsConstructor
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;


    @Enumerated(EnumType.STRING)
    private RequestStatus status;


    private LocalDate selectedDate;
    private LocalTime selectedStartTime;
    private LocalTime selectedEndTime;

    private LocalDate actualDate;
    private LocalTime actualTime;

    private String comment;

    private BigDecimal estimatedPrice;
    private BigDecimal actualPrice;
}
