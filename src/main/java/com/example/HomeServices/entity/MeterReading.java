package com.example.HomeServices.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "meter_readings")
@Getter
@Setter
public class MeterReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Связь с пользователем
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "reading_month")
    private LocalDate readingMonth;

    private Integer meter1;
    private Integer meter2;
    private Integer meter3;
    private Integer meter4;

    // Метод для расчета общего расхода
    public Integer getTotalConsumption() {
        return (meter1 != null ? meter1 : 0) +
                (meter2 != null ? meter2 : 0) +
                (meter3 != null ? meter3 : 0) +
                (meter4 != null ? meter4 : 0);
    }
}