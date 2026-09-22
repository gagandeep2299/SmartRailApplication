package com.Projects.SmartRailApplication.booking.dto;

import java.time.LocalDateTime;

import com.Projects.SmartRailApplication.booking.Entities.enums.BookingStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class bookingReponse {
    private Long id;
    private Long user_id;
    private Long train_id;
    private LocalDateTime travelDateTime;
    private BookingStatus bookingStatus;
}
