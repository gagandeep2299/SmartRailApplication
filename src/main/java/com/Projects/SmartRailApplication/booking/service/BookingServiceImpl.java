package com.Projects.SmartRailApplication.booking.service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Projects.SmartRailApplication.auth.entities.User;
import com.Projects.SmartRailApplication.booking.Entities.Booking;
import com.Projects.SmartRailApplication.booking.Entities.BookingSeat;
import com.Projects.SmartRailApplication.booking.Entities.enums.BookingStatus;
import com.Projects.SmartRailApplication.booking.dto.bookingReponse;
import com.Projects.SmartRailApplication.booking.dto.bookingRequest;
import com.Projects.SmartRailApplication.booking.repository.bookingRepository;
import com.Projects.SmartRailApplication.booking.repository.bookingSeatRepository;
import com.Projects.SmartRailApplication.train.Entities.Coach;
import com.Projects.SmartRailApplication.train.Entities.Fare;
import com.Projects.SmartRailApplication.train.Entities.RouteStop;
import com.Projects.SmartRailApplication.train.Entities.Seat;
import com.Projects.SmartRailApplication.train.Entities.Station;
import com.Projects.SmartRailApplication.train.Entities.train;
import com.Projects.SmartRailApplication.train.Repository.*;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service 
public class BookingServiceImpl implements BookingServices {

    @Autowired 
    private stationRepository stationRepository; 
    @Autowired
    private trainRepository trainRepository;
    @Autowired
    private routeStopRepository routeStopRepository;
    @Autowired
    private seatRepository seatRepository;
    @Autowired
    private fareRepository fareRepository;
    @Autowired
    private bookingRepository bookingRepository;
    @Autowired 
    private bookingSeatRepository bookingSeatRepository;


    @Override
    public bookingReponse createBooking(bookingRequest bookingRequest,User user) throws EntityNotFoundException {
        train train = trainRepository.findById(bookingRequest.getTrain_id()).orElseThrow(() -> new EntityNotFoundException("Train not found with ID: " + bookingRequest.getTrain_id()));
        Station SourceStation = stationRepository.findById(bookingRequest.getSource_station_id()).orElseThrow(() -> new EntityNotFoundException("Source station not found with ID: " + bookingRequest.getSource_station_id()));
        Station DestinationStation = stationRepository.findById(bookingRequest.getDestination_station_id()).orElseThrow(() -> new EntityNotFoundException("Destination station not found with ID: " + bookingRequest.getDestination_station_id()));
        if(SourceStation.getId().equals(DestinationStation.getId())) {
            throw new IllegalArgumentException("Source and destination stations cannot be the same.");
        }

        RouteStop sourceRouteStop = routeStopRepository.findByTrain_IdAndStation_Id(bookingRequest.getTrain_id(), bookingRequest.getSource_station_id())
                .orElseThrow(() -> new EntityNotFoundException("Source station is not part of the train's route."));
        
        RouteStop destinationRouteStop = routeStopRepository.findByTrain_IdAndStation_Id(bookingRequest.getTrain_id(), bookingRequest.getDestination_station_id())
                .orElseThrow(() -> new EntityNotFoundException("Destination station is not part of the train's route."));
        
        if(sourceRouteStop.getStopSequence() >= destinationRouteStop.getStopSequence()) {
            throw new IllegalArgumentException("Source station must come before destination station in the train's route");
        }

        List<Long> seatIds = bookingRequest.getSeatIds();
        if(seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("At least one seat must be selected for booking.");
        } else if(new HashSet<>(seatIds).size() != seatIds.size()) {
            throw new IllegalArgumentException("Duplicate seat IDs found in the booking request.");
        }
        Booking booking = new Booking();
        booking.setUser(user); 
        booking.setTrain(train);
        booking.setSourceStation(SourceStation);
        booking.setDestinationStation(DestinationStation);
        booking.setTravelDateTime(bookingRequest.getTravelDateTime());
        booking.setBookingStatus(BookingStatus.CREATED);
        booking.setTotalPrice(BigDecimal.ZERO);

        for(Long seatId : seatIds) {
            Seat seat = seatRepository.findById(seatId).orElseThrow(() -> new EntityNotFoundException("Seat not found with ID: " + seatId));
            Coach coach = seat.getCoachId();
            
            if(!coach.getTrainId().getId().equals(train.getId())) {
                throw new IllegalArgumentException("Seat with ID: " + seatId + " does not belong to the selected train.");
            }
            Fare fare = fareRepository.findByTrainId_IdAndCoachTypeAndSourceStationid_IdAndDestinationStationid_Id(
                train.getId(), coach.getCoachType(), SourceStation.getId(), DestinationStation.getId())
                .orElseThrow(() -> new EntityNotFoundException("Fare not found for the selected seat type."));
                
                System.out.println("Reach here");
            BookingSeat bookingSeat = new BookingSeat();
            bookingSeat.setBookingId(booking);
            bookingSeat.setSeatId(seat);
            bookingSeat.setPrice(fare.getFareAmount());
            bookingSeat.setBookingStatus(BookingStatus.CONFIRMED);
            bookingSeat=bookingSeatRepository.save(bookingSeat);
            booking.getBookingSeats().add(bookingSeat);
            booking.setTotalPrice(booking.getTotalPrice().add(fare.getFareAmount()));
        }

        System.out.println("Total Price: " + booking.getTotalPrice());
        booking = bookingRepository.save(booking);
        System.out.println(booking.getId());
        /*for(BookingSeat bookingSeat: booking.getBookingSeats()){
            bookingSeat.setBookingId(booking);
            bookingSeat.setId(bookingSeatRepository.save(bookingSeat).getId());
        }*/
        return mapToBookingResponse(booking);

    }

    private bookingReponse mapToBookingResponse(Booking booking) {
        bookingReponse response = new bookingReponse();
        response.setBookingId(booking.getId());
        response.setUser_id(booking.getUser().getId());
        response.setTrain_id(booking.getTrain().getId());
        response.setTravelDateTime(booking.getTravelDateTime());
        response.setBookingStatus(booking.getBookingStatus());
        response.setTotalPrice(booking.getTotalPrice());
        return response;
    }
    
}
