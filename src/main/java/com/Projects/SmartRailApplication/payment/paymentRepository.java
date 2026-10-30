package com.Projects.SmartRailApplication.payment;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Projects.SmartRailApplication.payment.Entities.payment;

import jakarta.persistence.LockModeType;

@Repository 
public interface paymentRepository extends JpaRepository<payment, Long> {
    boolean existsByIdempotencyKey(String key);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM payment p WHERE p.id = :id")
    Optional<payment> findByIdForUpdate(@Param("id") Long id);

    @Lock (LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT p FROM payment p
    WHERE p.booking_Id.id = :bookingId
      AND p.idempotencyKey = :idempotencyKey
""")
    Optional<payment> findByBookingAndKeyForUpdate(Long bookingId, String idempotencyKey);
}
