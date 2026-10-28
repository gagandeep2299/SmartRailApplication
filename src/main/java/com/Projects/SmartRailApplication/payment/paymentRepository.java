package com.Projects.SmartRailApplication.payment;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import com.Projects.SmartRailApplication.payment.Entities.payment;

import jakarta.persistence.LockModeType;

@Repository 
public interface paymentRepository extends JpaRepository<payment, Long> {
    boolean existsByIdempotencyKey(String key);

    @Lock (LockModeType.PESSIMISTIC_WRITE)
    Optional<payment> findByIdForUpdate(Long id);
}
