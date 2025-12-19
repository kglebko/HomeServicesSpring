package com.example.HomeServices.service;

import com.example.HomeServices.entity.Bill;
import com.example.HomeServices.entity.Payment;
import com.example.HomeServices.entity.User;
import com.example.HomeServices.repository.BillRepository;
import com.example.HomeServices.repository.PaymentRepository;
import com.example.HomeServices.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BillRepository billRepository;
    private final UserRepository userRepository;

    @Transactional
    public void pay(Long billId, BigDecimal amount) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Счет не найден"));

        User user = userRepository.findById(bill.getUserId())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        // Проверяем, не превышает ли оплата сумму счета
        BigDecimal totalPaid = bill.getPayments().stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal remainingAmount = bill.getAccruedAmount().subtract(totalPaid);

        if (amount.compareTo(remainingAmount) > 0) {
            throw new RuntimeException("Сумма оплаты превышает задолженность");
        }

        // Создаем платеж
        Payment payment = new Payment();
        payment.setBill(bill);
        payment.setUser(user);
        payment.setAmount(amount);
        payment.setPaidDate(LocalDate.now());

        paymentRepository.save(payment);

        // Обновляем статус счета, если полностью оплачено
        BigDecimal newTotalPaid = totalPaid.add(amount);
        if (newTotalPaid.compareTo(bill.getAccruedAmount()) >= 0) {
            bill.setStatus("Оплачено");
        } else {
            bill.setStatus("Частично оплачено");
        }

        billRepository.save(bill);
    }
}