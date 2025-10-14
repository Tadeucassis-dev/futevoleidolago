package com.futevoleidolago.backend.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;
    private Double amount;
    private String status; // PENDING, COMPLETED, FAILED
    private String qrCode; // Simulated QR Code (base64 or URL)
    private LocalDateTime createdAt;
}