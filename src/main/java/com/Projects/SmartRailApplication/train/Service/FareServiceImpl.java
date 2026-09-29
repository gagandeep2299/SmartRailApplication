package com.Projects.SmartRailApplication.train.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Projects.SmartRailApplication.train.Entities.Fare;
import com.Projects.SmartRailApplication.train.Entities.RouteStop;
import com.Projects.SmartRailApplication.train.Entities.Station;
import com.Projects.SmartRailApplication.train.Entities.train;
import com.Projects.SmartRailApplication.train.Entities.enums.CoachType;
import com.Projects.SmartRailApplication.train.dto.FareRequest;
import com.Projects.SmartRailApplication.train.dto.FareResponse;

import jakarta.persistence.EntityNotFoundException;

import com.Projects.SmartRailApplication.train.Repository.*;

@Service
public class FareServiceImpl implements FareService {
    @Autowired
    private fareRepository fareRepository;

    @Autowired
    private trainRepository trainRepository;

    @Autowired
    private stationRepository stationRepository;

    @Autowired
    private coachRepository coachRepository;

    @Autowired
    private routeStopRepository routeStopRepository;

    @Override
    public FareResponse createFare(FareRequest fareRequest) {
        train train = trainRepository.findById(fareRequest.getTrainId())
                .orElseThrow(() -> new EntityNotFoundException("Train not found with ID: " + fareRequest.getTrainId()));
        Station sourceStation = stationRepository.findById(fareRequest.getSourceStationid())
                .orElseThrow(() -> new EntityNotFoundException("Source Station not found with ID: " + fareRequest.getSourceStationid()));
        Station destinationStation = stationRepository.findById(fareRequest.getDestinationStationid())
                .orElseThrow(() -> new EntityNotFoundException("Destination Station not found with ID: " + fareRequest.getDestinationStationid()));
        Optional<RouteStop> sourceRoute = routeStopRepository.findByTrain_IdAndStation_Id(fareRequest.getTrainId(), fareRequest.getSourceStationid());
        Optional<RouteStop> destinationRoute = routeStopRepository.findByTrain_IdAndStation_Id(fareRequest.getTrainId(), fareRequest.getDestinationStationid());
        if(sourceRoute.isEmpty()) {
            throw new IllegalArgumentException("Train does not stop at the destination station");
        } else if(destinationRoute.isEmpty()) {
            throw new IllegalArgumentException("Train does not stop at the source station");
        } else if(sourceRoute.get().getStopSequence() >= destinationRoute.get().getStopSequence()) {
            throw new IllegalArgumentException("Source station must come before destination station in the train route");
        }

        fareRepository.findByTrainIdAndCoachTypeAndSourceStationidAndDestinationStationid(
                fareRequest.getTrainId(),
                fareRequest.getCoachType(),
                fareRequest.getSourceStationid(),
                fareRequest.getDestinationStationid()
        ).ifPresent(existingFare -> {
            throw new IllegalArgumentException("Fare already exists for this train, coach type, source station, and destination station");
        });

        Fare fare = new Fare();
        fare.setTrainId(train);
        fare.setCoachType(fareRequest.getCoachType());
        fare.setSourceStationid(sourceStation);
        fare.setDestinationStationid(destinationStation);
        fare.setFareAmount(fareRequest.getFareAmount());

        return mapToFareResponse(fareRepository.save(fare));
        
    }
    @Override
    public FareResponse getFare(Long trainId, Long sourceStationId, Long destinationStationId, CoachType coachType) {
        trainRepository.findById(trainId)
                .orElseThrow(() -> new EntityNotFoundException("Train not found with ID: " + trainId));
        stationRepository.findById(sourceStationId)
                .orElseThrow(() -> new EntityNotFoundException("Source Station not found with ID: " + sourceStationId));
        stationRepository.findById(destinationStationId)
                .orElseThrow(() -> new EntityNotFoundException("Destination Station not found with ID: " + destinationStationId));
        Fare fare = fareRepository.findByTrainIdAndCoachTypeAndSourceStationidAndDestinationStationid(
                trainId, coachType, sourceStationId, destinationStationId)
                .orElseThrow(() -> new EntityNotFoundException("Fare not found for the given parameters"));

        return mapToFareResponse(fare);
    }

    @Override
    public FareResponse getFareById(Long id) {
        Fare fare = fareRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fare not found with ID: " + id));
        return mapToFareResponse(fare);
    }

    @Override
    public FareResponse updateFare(Long id, FareRequest fareRequest) {
        Fare fare = fareRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fare not found with ID: " + id));
        train train = trainRepository.findById(fareRequest.getTrainId())
                .orElseThrow(() -> new EntityNotFoundException("Train not found with ID: " + fareRequest.getTrainId()));
        Station sourceStation = stationRepository.findById(fareRequest.getSourceStationid())
                .orElseThrow(() -> new EntityNotFoundException("Source Station not found with ID: " + fareRequest.getSourceStationid()));
        Station destinationStation = stationRepository.findById(fareRequest.getDestinationStationid())
                .orElseThrow(() -> new EntityNotFoundException("Destination Station not found with ID: " + fareRequest.getDestinationStationid()));
        Optional<RouteStop> sourceRoute = routeStopRepository.findByTrain_IdAndStation_Id(fareRequest.getTrainId(), fareRequest.getSourceStationid());
        Optional<RouteStop> destinationRoute = routeStopRepository.findByTrain_IdAndStation_Id(fareRequest.getTrainId(), fareRequest.getDestinationStationid());
        if(sourceRoute.isEmpty()) {
            throw new IllegalArgumentException("Train does not stop at the destination station");
        } else if(destinationRoute.isEmpty()) {
            throw new IllegalArgumentException("Train does not stop at the source station");
        } else if(sourceRoute.get().getStopSequence() >= destinationRoute.get().getStopSequence()) {
            throw new IllegalArgumentException("Source station must come before destination station in the train route");
        }

        fare.setTrainId(train);
        fare.setCoachType(fareRequest.getCoachType());
        fare.setSourceStationid(sourceStation);
        fare.setDestinationStationid(destinationStation);
        fare.setFareAmount(fareRequest.getFareAmount());

        return mapToFareResponse(fareRepository.save(fare));
    }
    @Override
    public void deleteFare(Long id) {
        Fare fare = fareRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fare not found with ID: " + id));
        fareRepository.delete(fare);
    }

    private FareResponse mapToFareResponse(Fare fare) {
        FareResponse fareResponse = new FareResponse();
        fareResponse.setId(fare.getId());
        fareResponse.setTrainId(fare.getTrainId().getId());
        fareResponse.setCoachType(fare.getCoachType());
        fareResponse.setSourceStationid(fare.getSourceStationid().getId());
        fareResponse.setDestinationStationid(fare.getDestinationStationid().getId());
        fareResponse.setFareAmount(fare.getFareAmount());
        return fareResponse;
    }
    
}
