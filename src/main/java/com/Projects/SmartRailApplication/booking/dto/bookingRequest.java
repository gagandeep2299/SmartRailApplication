package com.Projects.SmartRailApplication.booking.dto;

import java.time.LocalDateTime;

import com.Projects.SmartRailApplication.booking.Entities.enums.BookingStatus;

import lombok.Data;

@Data 
public class bookingRequest {
    private Long id;
    private Long user_id;
    private Long train_id;
    private LocalDateTime travelDateTime;
    private BookingStatus bookingStatus;
}
