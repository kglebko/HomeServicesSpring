package com.example.HomeServices.controller;

import com.example.HomeServices.entity.Bill;
import com.example.HomeServices.entity.MeterReading;
import com.example.HomeServices.entity.User;
import com.example.HomeServices.repository.BillRepository;
import com.example.HomeServices.repository.MeterReadingRepository;
import com.example.HomeServices.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class FinanceController {

    private final UserRepository userRepository;
    private final BillRepository billRepository;
    private final MeterReadingRepository meterReadingRepository;

    @GetMapping("/user/{userId}")
    public User getUser(@PathVariable Long userId) {
        return userRepository.findById(userId).orElseThrow();
    }

    @GetMapping("/bills/{userId}")
    public List<Bill> getBills(@PathVariable Long userId) {
        return billRepository.findByUserIdOrderByPeriodDesc(userId);
    }

    @GetMapping("/meters/{userId}")
    public List<MeterReading> getMeters(@PathVariable Long userId) {
        return meterReadingRepository.findByUserIdOrderByReadingMonthDesc(userId);
    }
}