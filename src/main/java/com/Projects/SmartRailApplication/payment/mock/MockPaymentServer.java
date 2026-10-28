package com.Projects.SmartRailApplication.payment.mock;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.Projects.SmartRailApplication.payment.dto.MockPaymentRequest;
import com.Projects.SmartRailApplication.payment.dto.MockPaymentResponse;

@Service
public class MockPaymentServer {
    private final ConcurrentMap<String, MockPaymentResponse> transactions = new ConcurrentHashMap<>();

    @Value("${mock.payment.default-outcome:SUCCESS}")
    private MockPaymentOutcome defaultOutcome;

    public MockPaymentResponse charge(MockPaymentRequest request) {
        return transactions.compute(request.idempotencyKey(), (key, existing) -> {
            if (existing != null) {
                if (existing.amount().compareTo(request.amount()) != 0) {
                    throw new IllegalArgumentException("Idempotency key was already used with a different amount");
                }
                return existing;
            }

            MockPaymentOutcome outcome = request.outcome() == null ? defaultOutcome : request.outcome();
            return new MockPaymentResponse(request.amount(), outcome, "mock-" + UUID.randomUUID());
        });
    }
}