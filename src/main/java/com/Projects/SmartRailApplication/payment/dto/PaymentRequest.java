package com.Projects.SmartRailApplication.payment.dto;

import jakarta.validation.constraints.NotBlank;

public record PaymentRequest(
    @NotBlank(message = "Idemtotency key is required")
    String idempotencyKey
) {
}