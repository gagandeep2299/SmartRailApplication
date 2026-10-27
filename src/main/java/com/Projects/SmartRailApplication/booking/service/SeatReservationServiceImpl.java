package com.Projects.SmartRailApplication.booking.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.Projects.SmartRailApplication.booking.Entities.Booking;
import com.Projects.SmartRailApplication.booking.Entities.BookingSeat;
import com.Projects.SmartRailApplication.booking.Entities.SeatReservation;
import com.Projects.SmartRailApplication.booking.Entities.enums.BookingStatus;
import com.Projects.SmartRailApplication.booking.Entities.enums.ReservationStatus;
import com.Projects.SmartRailApplication.booking.dto.ReservationRequest;
import com.Projects.SmartRailApplication.booking.dto.ReservationResponse;
import com.Projects.SmartRailApplication.booking.repository.bookingRepository;
import com.Projects.SmartRailApplication.booking.repository.bookingSeatRepository;
import com.Projects.SmartRailApplication.booking.repository.seatReservationRepository;
import com.Projects.SmartRailApplication.train.Repository.seatRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
@Service 
public class SeatReservationServiceImpl implements SeatReservationService{

    private final seatReservationRepository reserveRepository;
    private final seatRepository seatRepository;
    private final bookingSeatRepository bookingseatRepository;
    private final bookingRepository bookingRepository;
    

    @Transactional 
    public ReservationResponse reserveSeat(ReservationRequest request){

        if(request.journeyDate() ==null ||
            request.fromStopSequence() == null || 
           request.toStopSequence() == null || 
           request.fromStopSequence()>=request.toStopSequence()){
                throw new IllegalArgumentException("Invalid journey segment");
           }
        
        BookingSeat bookingSeat = bookingseatRepository.findById(request.bookingSeatId()).orElseThrow(()->new EntityNotFoundException());

        Long seatId = bookingSeat.getSeatId().getId();

        seatRepository.findByIdForUpdate(seatId).
                        orElseThrow(()->new EntityNotFoundException());

        LocalDateTime now = LocalDateTime.now();

        boolean alreadyReserved = reserveRepository.existsConflictingReservation
                                    (seatId, request.journeyDate(), request.fromStopSequence(),
                                     request.toStopSequence(), ReservationStatus.CONFIRMED, ReservationStatus.HELD, now);
        if(alreadyReserved){
            throw new IllegalStateException("Seat is not available for the journey");
        }

        SeatReservation reservation = new SeatReservation();
        reservation.setBookingSeat(bookingSeat);
        reservation.setJourneyDate(request.journeyDate());
        reservation.setFromStopSequence(request.fromStopSequence());
        reservation.setToStopSequence(request.toStopSequence());
        reservation.setStatus(ReservationStatus.HELD);
        reservation.setExpiresAt(now.plusMinutes(10));

        SeatReservation saved = reserveRepository.save(reservation);
        return toResponse(saved);
    }

    @Override
    public boolean isSeatReserved(Long seatId, LocalDate travelDate, Long fromStopSequence, Long toStopSequence) {
        LocalDateTime now = LocalDateTime.now();
        return reserveRepository.existsConflictingReservation(seatId, travelDate, fromStopSequence, toStopSequence, ReservationStatus.CONFIRMED, ReservationStatus.HELD, now);
    }
    @Override 
    @Transactional 
    public void confirmedBookingAfterPayment(Booking booking){
        List<SeatReservation> reservations = reserveRepository.findByBookingId(booking.getId());

        for(SeatReservation reservation : reservations){
            reservation.confirm();
            reservation.getBookingSeat().setBookingStatus(BookingStatus.CONFIRMED);
        }

        booking.setBookingStatus(BookingStatus.CONFIRMED);
        reserveRepository.saveAll(reservations);
        bookingRepository.save(booking);
    }

     private ReservationResponse toResponse(
            SeatReservation reservation) {

        return new ReservationResponse(
                reservation.getId(),
                reservation.getBookingSeat().getId(),
                reservation.getJourneyDate(),
                reservation.getFromStopSequence(),
                reservation.getToStopSequence(),
                reservation.getStatus(),
                reservation.getExpiresAt()
        );
    }

 
}
