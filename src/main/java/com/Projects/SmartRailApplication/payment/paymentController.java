package com.Projects.SmartRailApplication.payment;

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
        try{
            return ResponseEntity.ok().body(paymentService.initiatePayment(bookingId, paymentRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
}
