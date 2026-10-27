package com.Projects.SmartRailApplication.booking.service;

import org.springframework.stereotype.Service;

import com.Projects.SmartRailApplication.booking.Entities.Booking;
import com.Projects.SmartRailApplication.booking.dto.ReservationRequest;
import com.Projects.SmartRailApplication.booking.dto.ReservationResponse;

@Service
public interface SeatReservationService {
    public ReservationResponse reserveSeat(ReservationRequest request);
    public void confirmedBookingAfterPayment(Booking booking);
    public boolean isSeatReserved(Long seatId, java.time.LocalDate travelDate, Long fromStopSequence, Long toStopSequence);

}
