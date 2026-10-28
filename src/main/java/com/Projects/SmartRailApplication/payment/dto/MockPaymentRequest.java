package com.Projects.SmartRailApplication.payment.dto;

import java.math.BigDecimal;

import com.Projects.SmartRailApplication.payment.mock.MockPaymentOutcome;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MockPaymentRequest(
        @NotNull @DecimalMin(value = "0.01", message = "Amount must be greater than zero") BigDecimal amount,
        @NotBlank(message = "Idempotency key is required") String idempotencyKey,
        MockPaymentOutcome outcome) {
}