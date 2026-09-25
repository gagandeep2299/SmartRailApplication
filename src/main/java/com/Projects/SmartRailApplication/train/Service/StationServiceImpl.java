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
                .map(station -> new StationResponse(station.getName(),station.getState(), station.getCity(),station.getCode(),station.isActive()))
                .toList();
    }    
    @Override 
    public StationResponse getStationById(Long id) {
        Station station = stationRepository.findById(id).orElse(null);
        if(station == null) {
            throw new EntityNotFoundException("Station not found with id: " + id);
        }
            return new StationResponse(station.getName(), station.getState(), station.getCity(), station.getCode(), station.isActive());
        
    }
    @Override
    public StationResponse getStationByCode(String code) {
        Station station = stationRepository.findByCode(code);
        if(station == null) {
            throw new EntityNotFoundException("Station not found with code: " + code);
        }
            return new StationResponse(station.getName(), station.getState(), station.getCity(), station.getCode(), station.isActive());
    }

    @Override 
    public List<StationResponse> searchStations(String query) {
        List<Station> stations = stationRepository.findByNameContainingIgnoreCase(query);
        return stations.stream()
                .filter(station -> station.getName().toLowerCase().contains(query.toLowerCase()) ||
                                   station.getState().toLowerCase().contains(query.toLowerCase()) ||
                                   station.getCity().toLowerCase().contains(query.toLowerCase()) ||
                                   station.getCode().toLowerCase().contains(query.toLowerCase()))
                .map(station -> new StationResponse(station.getName(),station.getState(), station.getCity(),station.getCode(),station.isActive()))
                .toList();
    }
    @Override 
    public StationResponse updateStation(Long id, StationRequest station){
        Station existingStation = stationRepository.findById(id).orElse(null);
            if(existingStation == null) {
                throw new EntityNotFoundException("Station not found with id: " + id);
            }
            existingStation.setName(station.getName());
            existingStation.setState(station.getState());
            existingStation.setCity(station.getCity());
            existingStation.setCode(station.getCode());
            existingStation.setActive(station.isActive());
            stationRepository.save(existingStation);
            return new StationResponse(existingStation.getName(), existingStation.getState(), existingStation.getCity(), existingStation.getCode(), existingStation.isActive());

        }
}
