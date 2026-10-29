package com.Projects.SmartRailApplication.payment;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Projects.SmartRailApplication.payment.dto.PaymentRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping ("/api/v1")
@RequiredArgsConstructor 
public class paymentController {

    private final paymentService paymentService;

    @PostMapping ("/bookings/{bookingId}/payments")
    public ResponseEntity<?> initiatePayment(@PathVariable Long bookingId,@Valid @RequestBody PaymentRequest paymentRequest){
        try {
        return ResponseEntity.ok(
                paymentService.initiatePayment(
                        bookingId,
                        paymentRequest
                )
        );
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    } catch (DuplicateKeyException e) {
        return ResponseEntity.status(409).body(e.getMessage());
    } catch (IllegalStateException e) {
        return ResponseEntity.status(409).body(e.getMessage());
    }
    }

    @PostMapping ("/bookings/{bookingId}/payments/{idempotencyKey}")
    public ResponseEntity<?> getPaymentStatus(@PathVariable Long bookingId, @PathVariable String idempotencyKey) {
        try {
            return ResponseEntity.ok(
                    paymentService.getPayment(bookingId, idempotencyKey)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }
    
}
