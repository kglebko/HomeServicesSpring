package com.example.HomeServices.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bills")
@Getter
@Setter
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    private LocalDate period;

    @Column(name = "accrued_amount")
    private BigDecimal accruedAmount;

    @Column(name = "accrued_date")
    private LocalDate accruedDate;

    @Column(name = "meter_sum")
    private BigDecimal meterSum;

    @Column(name = "status", columnDefinition = "ENUM('Оплачено', 'Не оплачено')")
    private String status;

    // Связь с пользователем (без обратной связи, т.к. в таблице только user_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    // Связь с платежами
    @OneToMany(mappedBy = "bill", fetch = FetchType.LAZY)
    private List<Payment> payments = new ArrayList<>();

    // Конструкторы
    public Bill() {
    }

    public Bill(Long userId, LocalDate period, BigDecimal accruedAmount,
                LocalDate accruedDate, BigDecimal meterSum, String status) {
        this.userId = userId;
        this.period = period;
        this.accruedAmount = accruedAmount;
        this.accruedDate = accruedDate;
        this.meterSum = meterSum;
        this.status = status;
    }
}