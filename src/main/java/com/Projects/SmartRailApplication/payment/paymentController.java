package com.Projects.SmartRailApplication.payment;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Projects.SmartRailApplication.auth.entities.CustomUserDetails;
import com.Projects.SmartRailApplication.payment.dto.PaymentRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping ("/api/v1")
@RequiredArgsConstructor 
public class paymentController {

    private final paymentService paymentService;

    @PostMapping ("/bookings/{bookingId}/payments")
    public ResponseEntity<?> initiatePayment(@AuthenticationPrincipal CustomUserDetails userDetails,@PathVariable Long bookingId,@Valid @RequestBody PaymentRequest paymentRequest){
        return ResponseEntity.ok(
                paymentService.initiatePayment(
                        userDetails.getUser().getId(),
                        bookingId,
                        paymentRequest
                )
        );
    }

    @PostMapping ("/bookings/{bookingId}/payments/{idempotencyKey}")
    public ResponseEntity<?> getPaymentStatus(@AuthenticationPrincipal CustomUserDetails userDetails,@PathVariable Long bookingId, @PathVariable String idempotencyKey) {
        return ResponseEntity.ok(
                    paymentService.getPayment(
                            userDetails.getUser().getId(),
                            bookingId,
                            idempotencyKey
                    )
        );
       
    }
    
}
