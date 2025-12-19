package com.example.HomeServices.controller;

import com.example.HomeServices.dto.BillResponseDto;
import com.example.HomeServices.dto.MeterReadingDto;
import com.example.HomeServices.dto.PaymentHistoryDto;
import com.example.HomeServices.entity.Bill;
import com.example.HomeServices.entity.MeterReading;
import com.example.HomeServices.entity.User;
import com.example.HomeServices.service.PaymentService;
import com.example.HomeServices.repository.BillRepository;
import com.example.HomeServices.repository.MeterReadingRepository;
import com.example.HomeServices.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class FinanceController {

    private final UserRepository userRepository;
    private final BillRepository billRepository;
    private final MeterReadingRepository meterReadingRepository;
    private final PaymentService paymentService;

    // Получение информации о пользователе (без связанных сущностей)
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElse(null);

            if (user == null) {
                return ResponseEntity.ok(Collections.singletonMap("message", "Пользователь не найден"));
            }

            // Создаем простой объект без циклических ссылок
            var response = new java.util.HashMap<String, Object>();
            response.put("id", user.getId());
            response.put("firstName", user.getFirstName());
            response.put("lastName", user.getLastName());
            response.put("residentsCount", user.getResidentsCount());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    // Получение всех счетов пользователя (используем DTO)
    @GetMapping("/bills/{userId}")
    public ResponseEntity<?> getBills(@PathVariable Long userId) {
        try {
            List<BillResponseDto> bills = billRepository.findByUserIdOrderByPeriodDesc(userId)
                    .stream()
                    .map(bill -> new BillResponseDto(
                            bill.getId(),
                            bill.getUserId(),
                            bill.getPeriod(),
                            bill.getAccruedAmount(),
                            bill.getAccruedDate(),
                            bill.getMeterSum(),
                            bill.getStatus()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(bills != null ? bills : Collections.emptyList());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    // Получение показаний счетчиков (уже использует DTO)
    @GetMapping("/meters/{userId}")
    public ResponseEntity<?> getMeters(@PathVariable Long userId) {
        try {
            List<MeterReadingDto> meters = meterReadingRepository
                    .findByUser_IdOrderByReadingMonthDesc(userId)
                    .stream()
                    .map(m -> new MeterReadingDto(
                            m.getId(),
                            m.getReadingMonth(),
                            m.getMeter1(),
                            m.getMeter2(),
                            m.getMeter3(),
                            m.getMeter4()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(meters != null ? meters : Collections.emptyList());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    // История платежей
    @GetMapping("/payments/{userId}")
    public ResponseEntity<?> getPaymentHistory(@PathVariable Long userId) {
        try {
            List<PaymentHistoryDto> payments = billRepository.findByUserIdOrderByPeriodDesc(userId)
                    .stream()
                    .map(bill -> {
                        BigDecimal paidAmount = BigDecimal.ZERO;
                        LocalDate paidDate = null;

                        // Безопасный доступ к payments
                        if (bill.getPayments() != null) {
                            paidAmount = bill.getPayments().stream()
                                    .map(p -> p.getAmount())
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                            paidDate = bill.getPayments().stream()
                                    .map(p -> p.getPaidDate())
                                    .findFirst()
                                    .orElse(null);
                        }

                        return new PaymentHistoryDto(
                                bill.getPeriod(),
                                bill.getStatus(),
                                bill.getAccruedAmount(),
                                bill.getAccruedDate(),
                                paidAmount,
                                paidDate
                        );
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(payments != null ? payments : Collections.emptyList());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping("/current/{userId}")
    public ResponseEntity<?> getCurrentBill(@PathVariable Long userId) {
        Bill bill = billRepository
                .findFirstByUserIdAndStatusOrderByPeriodDesc(userId, "Не оплачено")
                .orElse(null);

        if (bill == null) {
            return ResponseEntity.ok(Collections.emptyMap());
        }

        return ResponseEntity.ok(
                new BillResponseDto(
                        bill.getId(),
                        bill.getUserId(),
                        bill.getPeriod(),
                        bill.getAccruedAmount(),
                        bill.getAccruedDate(),
                        bill.getMeterSum(),
                        bill.getStatus()
                )
        );
    }

    @PostMapping("/pay/{billId}")
    public ResponseEntity<?> payBill(
            @PathVariable Long billId,
            @RequestParam BigDecimal amount
    ) {
        paymentService.pay(billId, amount);
        return ResponseEntity.ok(Collections.singletonMap("message", "Оплачено"));
    }

}