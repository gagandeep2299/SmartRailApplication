package com.Projects.SmartRailApplication.payment.mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.Projects.SmartRailApplication.payment.dto.MockPaymentRequest;

class MockPaymentServerTest {

    @Test
    void chargeUsesRequestedOutcomeAndReplaysSameTransactionForIdempotentRequest() {
        MockPaymentServer server = new MockPaymentServer();
        ReflectionTestUtils.setField(server, "defaultOutcome", MockPaymentOutcome.PENDING);
        MockPaymentRequest request = new MockPaymentRequest(
                new BigDecimal("42.50"), "booking-123-payment", MockPaymentOutcome.SUCCESS);

        var firstResponse = server.charge(request);
        var replayedResponse = server.charge(request);

        assertEquals(MockPaymentOutcome.SUCCESS, firstResponse.outcome());
        assertEquals("42.50", firstResponse.amount().toPlainString());
        assertEquals(firstResponse, replayedResponse);
    }

    @Test
    void chargeRejectsIdempotencyKeyReusedWithDifferentAmount() {
        MockPaymentServer server = new MockPaymentServer();
        server.charge(new MockPaymentRequest(new BigDecimal("10.00"), "same-key", MockPaymentOutcome.SUCCESS));

        assertThrows(IllegalArgumentException.class, () -> server.charge(
                new MockPaymentRequest(new BigDecimal("11.00"), "same-key", MockPaymentOutcome.SUCCESS)));
    }
}