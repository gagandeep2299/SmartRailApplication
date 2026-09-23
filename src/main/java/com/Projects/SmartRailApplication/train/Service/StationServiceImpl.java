package com.Projects.SmartRailApplication.train.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Projects.SmartRailApplication.train.dto.StationRequest;
import com.Projects.SmartRailApplication.train.dto.StationResponse;

import jakarta.persistence.EntityNotFoundException;

import com.Projects.SmartRailApplication.train.Entities.Station;
import com.Projects.SmartRailApplication.train.Repository.stationRepository;

@Service 
public class StationServiceImpl implements StationService {

    @Autowired 
    private stationRepository stationRepository;

    @Override
    public boolean addStation(StationRequest station) {
        try{
            Station newStation = new Station();
            newStation.setName(station.getName());
            newStation.setState(station.getState());
            newStation.setCity(station.getCity());
            newStation.setCode(station.getCode());
            stationRepository.save(newStation);
        } catch (Exception e) {
            // Handle the exception, e.g., log it or rethrow it
            System.err.println("Error saving station: " + e.getMessage());
            return false; // Indicate failure
        }
        return true; // Indicate success
    }

    @Override
    public List<StationResponse> getAllStations() {
        List<Station> stations = stationRepository.findAll();
        return stations.stream()
                .map(station -> new StationResponse(station.getName(),station.getState(), station.getCity(),station.getCode()))
                .toList();
    }    
    @Override 
    public StationResponse getStationById(Long id) {
        Station station = stationRepository.findById(id).orElse(null);
        if(station == null) {
            throw new EntityNotFoundException("Station not found with id: " + id);
        }
            return new StationResponse(station.getName(), station.getState(), station.getCity(), station.getCode());
        
    }
}
