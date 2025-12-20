package com.example.HomeServices.service;

import com.example.HomeServices.entity.Bill;
import com.example.HomeServices.entity.Payment;
import com.example.HomeServices.repository.BillRepository;
import com.example.HomeServices.repository.PaymentRepository;
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

    @Transactional
    public void pay(Long billId, BigDecimal amount) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Счет не найден"));

        Payment payment = new Payment();
        payment.setBill(bill);
        payment.setUser(bill.getUser());
        payment.setAmount(amount);
        payment.setPaidDate(LocalDate.now());

        paymentRepository.save(payment);

        bill.setStatus("Оплачено");
        billRepository.save(bill);
    }
}
