package com.example.HomeServices.repository;

import com.example.HomeServices.entity.MeterReading;
import com.example.HomeServices.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MeterReadingRepository extends JpaRepository<MeterReading, Long> {

    List<MeterReading> findByUser_IdOrderByReadingMonthDesc(Long userId);

    Optional<MeterReading> findFirstByUser_IdOrderByReadingMonthDesc(Long userId);

    @Query("SELECT m FROM MeterReading m WHERE m.user = :user AND m.readingMonth = :month")
    Optional<MeterReading> findByUserAndMonth(@Param("user") User user, @Param("month") LocalDate month);
}