package com.Projects.SmartRailApplication.train.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Projects.SmartRailApplication.booking.service.SeatReservationService;
import com.Projects.SmartRailApplication.train.Entities.Coach;
import com.Projects.SmartRailApplication.train.Entities.RouteStop;
import com.Projects.SmartRailApplication.train.Entities.Seat;
import com.Projects.SmartRailApplication.train.Entities.Station;
import com.Projects.SmartRailApplication.train.Entities.train;
import com.Projects.SmartRailApplication.train.dto.CoachAvailabilityResponse;
import com.Projects.SmartRailApplication.train.dto.CoachAvailabilityResponse.CoachAvailabilityResponseBuilder;
import com.Projects.SmartRailApplication.train.dto.CoachResponse;
import com.Projects.SmartRailApplication.train.dto.TrainSearchResponse;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import com.Projects.SmartRailApplication.train.Repository.*;

@Service 
@Transactional
public class trainSearchServiceImpl implements trainSearchService {

    @Autowired 
    private   stationRepository stationRepository;
    @Autowired 
    private routeStopRepository routeStopRepository;
    @Autowired 
    private coachRepository coachRepository;
    @Autowired 
    private seatRepository seatRepository;
    @Autowired 
    private SeatReservationService seatReservationService;

    @Override
    public List<TrainSearchResponse> searchTrains(String sourceStationCode, String destinationStationCode,
            LocalDate date) {
        List<TrainSearchResponse> searchResults = new ArrayList<>();
        Station sourceStation = stationRepository.findByCode(sourceStationCode);
        if(sourceStation == null) {
            throw new EntityNotFoundException("Source station not found with code: " + sourceStationCode);  
        }
        Station destinationStation = stationRepository.findByCode(destinationStationCode);
        if(destinationStation == null) {
            throw new EntityNotFoundException("Destination station not found with code: " + destinationStationCode);  
        }

        List<RouteStop> sourceRouteStops = routeStopRepository.findByStation_Id(sourceStation.getId());
        for(RouteStop sourceRouteStop : sourceRouteStops) {
            train train = sourceRouteStop.getTrain();
            Optional<RouteStop> destinationRouteStopOpt = routeStopRepository.findByTrain_IdAndStation_Id(train.getId(), destinationStation.getId());

            if(destinationRouteStopOpt.isEmpty()) {
                continue; // Skip if the train does not stop at the destination station
            }
            if(sourceRouteStop.getStopSequence() >= destinationRouteStopOpt.get().getStopSequence()) {
                continue;
            }
            TrainSearchResponse response = buildTrainSearchResponse(train, sourceRouteStop, destinationRouteStopOpt.get());
            if(response!=null)
                searchResults.add(response);
        }
        return searchResults;
    }

    @Override 
    public List<Seat> isTrainAvailable(Long trainId, LocalDate travelDate, Long fromStopSequence, Long toStopSequence) {
        List<Coach> coaches = coachRepository.findByTrainId_Id(trainId);
        List<Seat> availableSeats = new ArrayList<>();
        for(Coach coach : coaches) {
            availableSeats = seatRepository.findByCoachId(coach);
            for(Seat seat : availableSeats) {
                boolean isReserved = seatReservationService.isSeatReserved(seat.getId(), travelDate, fromStopSequence, toStopSequence);
                if(isReserved) {
                   availableSeats.remove(seat); 
                }
            }
        }
        return availableSeats;
    }

    private TrainSearchResponse buildTrainSearchResponse(train train, RouteStop sourceRouteStop, RouteStop destinationRouteStop) {
        List<Coach> coaches = coachRepository.findByTrainId_Id(train.getId());
        List<CoachAvailabilityResponse> coachAvailabilityResponses = coaches.stream()
                .map(this::coachAvailabilityResponses)
                .toList();
        return TrainSearchResponse.builder()
                .id(train.getId())
                .trainName(train.getName())
                .trainNumber(train.getTrainNumber())
                .trainStatus(train.getStatus())
                .sourceStationCode(sourceRouteStop.getStation().getCode())
                .sourceStationCity(sourceRouteStop.getStation().getCity())
                .sourceStationName(sourceRouteStop.getStation().getName())
                .sourceStationState(sourceRouteStop.getStation().getState())
                .departureTime(sourceRouteStop.getDepartureTime())
                .destinationStationCode(destinationRouteStop.getStation().getCode())
                .destinationStationCity(destinationRouteStop.getStation().getCity())
                .destinationStationName(destinationRouteStop.getStation().getName())
                .destinationStationState(destinationRouteStop.getStation().getState())
                .ArrivalTime(destinationRouteStop.getArrivalTime())
                .coaches(coachAvailabilityResponses)
                .build();
    }
    private CoachAvailabilityResponse coachAvailabilityResponses(Coach coach) {
        List<Seat> availableSeats = seatRepository.findByCoachId(coach);
        long totalSeats = availableSeats.size();
        return CoachAvailabilityResponse.builder()
                .id(coach.getId())
                .trainId(coach.getTrainId().getId())
                .coachNumber(coach.getCoachNumber())
                .coachType(coach.getCoachType())
                .totalSeats(totalSeats)
                .availableSeats(totalSeats)
                .build();
    }

    
}
