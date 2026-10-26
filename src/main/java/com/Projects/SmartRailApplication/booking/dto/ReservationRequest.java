package com.Projects.SmartRailApplication.booking.dto;

import java.time.LocalDate;

public record ReservationRequest(
        /*Long bookingId,
        Long seatId,*/
        Long bookingSeatId,
        LocalDate journeyDate,
        Long fromStopSequence,
        Long toStopSequence
){}
