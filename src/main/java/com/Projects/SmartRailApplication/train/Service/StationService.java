package com.Projects.SmartRailApplication.train.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.Projects.SmartRailApplication.train.dto.StationRequest;
import com.Projects.SmartRailApplication.train.dto.StationResponse;

@Service 
public interface StationService {
    public boolean addStation(StationRequest station);
    public List<StationResponse> getAllStations();
    public StationResponse getStationById(Long id);
    public StationResponse getStationByCode(String code);
    public List<StationResponse> searchStations(String query);
    public StationResponse updateStation(Long id, StationRequest station); 
}
