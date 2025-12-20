package com.example.HomeServices.controller;

import com.example.HomeServices.dto.MeterReadingRequest;
import com.example.HomeServices.entity.MeterReading;
import com.example.HomeServices.entity.User;
import com.example.HomeServices.repository.MeterReadingRepository;
import com.example.HomeServices.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/meters")
@RequiredArgsConstructor
public class MeterReadingController {

    private final MeterReadingRepository meterReadingRepository;
    private final UserRepository userRepository;

    @PostMapping("/submit")
    public ResponseEntity<?> submitReading(@RequestBody MeterReadingRequest request) {
        try {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

            MeterReading reading = new MeterReading();
            reading.setUser(user);

            String monthStr = request.getMonth();
            if (monthStr == null || monthStr.isEmpty()) {
                throw new RuntimeException("Month не указан");
            }
            reading.setReadingMonth(LocalDate.parse(monthStr + "-01"));

            reading.setMeter1(request.getMeter1());
            reading.setMeter2(request.getMeter2());
            reading.setMeter3(request.getMeter3());
            reading.setMeter4(request.getMeter4());

            MeterReading saved = meterReadingRepository.save(reading);

            return ResponseEntity.ok(Map.of(
                    "message", "Показания успешно сохранены",
                    "id", saved.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
