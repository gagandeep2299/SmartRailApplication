package com.Projects.SmartRailApplication.booking.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.Projects.SmartRailApplication.booking.Entities.enums.ReservationStatus;
import com.Projects.SmartRailApplication.booking.repository.seatReservationRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SeatReservationExpiryScheduler {

    private final seatReservationRepository seatReservationRepo;
    private final SeatReservationService seatReservationService;
    
    @Scheduled(fixedDelay = 30000) // Run every 30 seconds
    public void expireHeldReservations() {
        List<Long> expiredReservationIds = seatReservationRepo.findExpiredReservationIds(ReservationStatus.HELD, LocalDateTime.now());

        for(Long reservationId : expiredReservationIds) {
            try{
                seatReservationService.expiredReservation(reservationId);
            } catch (Exception e) {
                System.out.println("Error expiring reservation with ID " + reservationId + ": " + e.getMessage());
            }
        }
    }
}
