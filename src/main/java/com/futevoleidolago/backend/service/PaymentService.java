package com.futevoleidolago.backend.service;

import com.futevoleidolago.backend.models.Payment;
import com.futevoleidolago.backend.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment createPayment(Long studentId, Double amount) {
        Payment payment = new Payment();
        payment.setStudentId(studentId);
        payment.setAmount(amount);
        payment.setStatus("PENDING");
        payment.setQrCode(generateMockQrCode()); // Simula QR Code
        payment.setCreatedAt(LocalDateTime.now());
        return paymentRepository.save(payment);
    }

    public List<Payment> getPaymentsByStudent(Long studentId) {
        return paymentRepository.findByStudentId(studentId);
    }

    public Payment getPaymentStatus(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    // Simula geração de QR Code (substituir por API real)
    private String generateMockQrCode() {
        return "data:image/png;base64," + new Random().ints(48, 48 + 10)
                .limit(100)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}