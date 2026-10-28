package com.Projects.SmartRailApplication.payment.dto;

import java.math.BigDecimal;

import com.Projects.SmartRailApplication.payment.mock.MockPaymentOutcome;

public record MockPaymentResponse(
        BigDecimal amount,
        MockPaymentOutcome outcome,
        String transactionReference) {
}