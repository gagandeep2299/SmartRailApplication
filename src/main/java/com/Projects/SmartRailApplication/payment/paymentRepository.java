package com.Projects.SmartRailApplication.payment;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Projects.SmartRailApplication.payment.Entities.payment;

public interface paymentRepository extends JpaRepository<payment, Long> {
    
}
