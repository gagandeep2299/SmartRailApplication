package com.Projects.SmartRailApplication.payment.dto;

import java.math.BigDecimal;

import com.Projects.SmartRailApplication.payment.Entities.PaymentStatus;

import lombok.Data;

@Data
public class PaymentRequest {
    private Long id;
    private Long bookingId;
    private BigDecimal amount;
    private PaymentStatus paymentStatus;
    private String transactionReference;
}