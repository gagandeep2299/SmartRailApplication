package com.Projects.SmartRailApplication.payment.mock;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Projects.SmartRailApplication.payment.dto.MockPaymentRequest;
import com.Projects.SmartRailApplication.payment.dto.MockPaymentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/mock-payment")
@RequiredArgsConstructor
public class MockPaymentController {
    private final MockPaymentServer mockPaymentServer;

    @PostMapping("/charges")
    public ResponseEntity<MockPaymentResponse> charge(@Valid @RequestBody MockPaymentRequest request) {
        return ResponseEntity.ok(mockPaymentServer.charge(request));
    }
}