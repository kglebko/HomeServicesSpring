package com.example.HomeServices.repository;

import com.example.HomeServices.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findByUserIdOrderByPeriodDesc(Long userId);

    Optional<Bill> findFirstByUserIdAndStatusOrderByPeriodDesc(Long userId, String status);

    List<Bill> findByUserId(Long userId);
}
