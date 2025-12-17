package com.example.HomeServices.repository;


import com.example.HomeServices.entity.MeterReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MeterReadingRepository extends JpaRepository<MeterReading, Long> {

    List<MeterReading> findByUserIdOrderByReadingMonthDesc(Long userId);

    List<MeterReading> findByUserId(Long userId);

    List<MeterReading> findByUserIdAndReadingMonthBetween(Long userId, LocalDate startDate, LocalDate endDate);

    Optional<MeterReading> findTopByUserIdOrderByReadingMonthDesc(Long userId);

    Optional<MeterReading> findByUserIdAndReadingMonth(Long userId, LocalDate readingMonth);

    @Query("SELECT mr FROM MeterReading mr WHERE mr.user.id = :userId AND YEAR(mr.readingMonth) = :year ORDER BY mr.readingMonth DESC")
    List<MeterReading> findByUserIdAndYear(@Param("userId") Long userId, @Param("year") int year);
}