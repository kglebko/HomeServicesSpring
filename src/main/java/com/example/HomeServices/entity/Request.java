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

    // Связь с услугой
    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    // Статус заявки
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    // Дата и время выбранного интервала
    private LocalDate selectedDate;
    private LocalTime selectedStartTime;
    private LocalTime selectedEndTime;

    // Точная дата и время оказания услуги
    private LocalDate actualDate;
    private LocalTime actualTime;

    // Комментарий пользователя
    private String comment;

    // Стоимость
    private BigDecimal estimatedPrice; // примерная стоимость
    private BigDecimal actualPrice;    // оплаченная стоимость
}
