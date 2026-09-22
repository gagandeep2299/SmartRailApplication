package com.Projects.SmartRailApplication.payment.dto;

import java.math.BigDecimal;

import com.Projects.SmartRailApplication.payment.Entities.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {
    private Long id;
    private Long bookingId;
    private BigDecimal amount;
    private PaymentStatus paymentStatus;
    private String transactionReference;
}