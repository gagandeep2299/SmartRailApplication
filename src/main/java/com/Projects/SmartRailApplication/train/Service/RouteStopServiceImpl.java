package com.Projects.SmartRailApplication.train.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Projects.SmartRailApplication.train.dto.RouteStopRequest;
import com.Projects.SmartRailApplication.train.dto.RouteStopResponse;

import jakarta.persistence.EntityNotFoundException;

import com.Projects.SmartRailApplication.train.Entities.RouteStop;
import com.Projects.SmartRailApplication.train.Entities.Station;
import com.Projects.SmartRailApplication.train.Entities.train;
import com.Projects.SmartRailApplication.train.Repository.routeStopRepository;
import com.Projects.SmartRailApplication.train.Repository.stationRepository;
import com.Projects.SmartRailApplication.train.Repository.trainRepository;

@Service 
public class RouteStopServiceImpl implements RouteStopService {

    @Autowired 
    private routeStopRepository routeStopRepository;
    @Autowired 
    private trainRepository trainRepository;
    @Autowired 
    private stationRepository stationRepository;

    @Override 
    public void addRouteStop(Long trainId, RouteStopRequest routeStopRequest) {
        train train = trainRepository.findById(trainId)
                .orElseThrow(() -> new EntityNotFoundException("Train not found with ID: " + trainId));
        Station station = stationRepository.findById(routeStopRequest.getStationId())
                .orElseThrow(() -> new EntityNotFoundException("Station not found with ID: " + routeStopRequest.getStationId()));
        
        if(routeStopRepository.existsByTrain_IdAndStopSequence(train.getId(), routeStopRequest.getStopSequence())) {
            throw new IllegalArgumentException("Route stop already exists for this train and stop sequence");
        }
        if(routeStopRequest.getArrivalTime() != null && routeStopRequest.getDepartureTime() != null 
            && routeStopRequest.getArrivalTime().isAfter(routeStopRequest.getDepartureTime())) {
            throw new IllegalArgumentException("Invalid arrival/departure time format.");
        }
        RouteStop routeStop = new RouteStop();
        routeStop.setTrain(train);
        routeStop.setStation(station);
        routeStop.setArrivalTime(routeStopRequest.getArrivalTime());
        routeStop.setDepartureTime(routeStopRequest.getDepartureTime());
        routeStop.setStopSequence(routeStopRequest.getStopSequence());
        routeStopRepository.save(routeStop);
        // Implementation for adding a route stop to a train
        // You can use the trainId and routeStopRequest to perform the necessary operations
    }
    @Override 
    public RouteStopResponse getRouteStopsById(Long id) {
        RouteStop routeStop = routeStopRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Route stop not found with ID: " + id));
        return mapToResponse(routeStop);
    }
    @Override
    public List<RouteStopResponse> getRouteStopsByTrain(Long trainId) {
    trainRepository.findById(trainId)
                .orElseThrow(() -> new EntityNotFoundException("Train not found with ID: " + trainId));

    return routeStopRepository
            .findByTrain_IdOrderByStopSequenceAsc(trainId)
            .stream()
            .map(this::mapToResponse)
            .toList();
}

    @Override
    public RouteStopResponse updateRouteStop(Long id, RouteStopRequest routeStopRequest) {
        RouteStop routeStop = routeStopRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Route stop not found with ID: " + id));
        Station station = stationRepository.findById(routeStopRequest.getStationId())
                .orElseThrow(() -> new EntityNotFoundException("Station not found with ID: " + routeStopRequest.getStationId()));
        
        if(routeStopRequest.getArrivalTime() != null && routeStopRequest.getDepartureTime() != null 
            && routeStopRequest.getArrivalTime().isAfter(routeStopRequest.getDepartureTime())) {
            throw new IllegalArgumentException("Invalid arrival/departure time format.");
        }
        routeStop.setStation(station);
        routeStop.setArrivalTime(routeStopRequest.getArrivalTime());
        routeStop.setDepartureTime(routeStopRequest.getDepartureTime());
        routeStop.setStopSequence(routeStopRequest.getStopSequence());
        routeStopRepository.save(routeStop);
        return mapToResponse(routeStop);
    }

    public void deleteRouteStop(Long id) {
        RouteStop routeStop = routeStopRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Route stop not found with ID: " + id));
        routeStopRepository.delete(routeStop);
    }
    

    private RouteStopResponse mapToResponse(RouteStop routeStop) {

    return RouteStopResponse.builder()
            .id(routeStop.getId())
            .trainId(routeStop.getTrain().getId())
            .stationId(routeStop.getStation().getId())
            .stationCode(routeStop.getStation().getCode())
            .stationName(routeStop.getStation().getName())
            .stationCity(routeStop.getStation().getCity())
            .stationState(routeStop.getStation().getState())
            .stopSequence(routeStop.getStopSequence())
            .arrivalTime(routeStop.getArrivalTime())
            .departureTime(routeStop.getDepartureTime())
            .build();
}
}
