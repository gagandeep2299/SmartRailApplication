package com.Projects.SmartRailApplication.payment;

import org.springframework.stereotype.Service;

import com.Projects.SmartRailApplication.payment.dto.PaymentRequest;
import com.Projects.SmartRailApplication.payment.dto.PaymentResponse;

@Service 
public interface paymentService {

    public PaymentResponse initiatePayment(Long bookingId, PaymentRequest paymentRequest);
    public PaymentResponse getPayment(Long bookingId, String idempotencyKey);
}
