package com.example.HomeServices.controller;

import com.example.HomeServices.entity.MeterReading;
import com.example.HomeServices.entity.User;
import com.example.HomeServices.repository.MeterReadingRepository;
import com.example.HomeServices.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
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

            //"YYYY-MM"
            reading.setReadingMonth(LocalDate.parse(request.getMonth() + "-01"));

            reading.setMeter1(request.getMeter1());
            reading.setMeter2(request.getMeter2());
            reading.setMeter3(request.getMeter3());
            reading.setMeter4(request.getMeter4());

            MeterReading saved = meterReadingRepository.save(reading);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Показания успешно сохранены");
            response.put("id", saved.getId());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}

@Data
class MeterReadingRequest {
    private Long userId;
    private Integer meter1;
    private Integer meter2;
    private Integer meter3;
    private Integer meter4;
    private String month;
}
