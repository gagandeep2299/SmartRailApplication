package com.Projects.SmartRailApplication.booking.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.Projects.SmartRailApplication.booking.Entities.enums.ReservationStatus;

public record ReservationResponse(
        Long reservationId,
        Long bookingSeatId,
        LocalDate journeyDate,
        Long fromStopSequence,
        Long toStopSequence,
        ReservationStatus status,
        LocalDateTime expiresAt
) {}
