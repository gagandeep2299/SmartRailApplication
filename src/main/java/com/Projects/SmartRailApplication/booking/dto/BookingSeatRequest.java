package com.Projects.SmartRailApplication.booking.dto;

import lombok.Data;

@Data
public class BookingSeatRequest {
    private Long bookingId;
    private Long seatId;
}