package com.Projects.SmartRailApplication.booking.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.Projects.SmartRailApplication.booking.Entities.BookingSeat;
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
    private Long bookingId;
    private Long user_id;
    private Long train_id;
    private LocalDate travelDateTime;
    private BookingStatus bookingStatus;
    private BigDecimal totalPrice;
    private List<BookingSeat> bookingSeats;
}
