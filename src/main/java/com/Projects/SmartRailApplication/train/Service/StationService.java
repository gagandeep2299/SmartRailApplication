package com.Projects.SmartRailApplication.train.Service;

import java.util.List;

import com.Projects.SmartRailApplication.train.dto.StationRequest;
import com.Projects.SmartRailApplication.train.dto.StationResponse;

public interface StationService {
    public boolean addStation(StationRequest station);
    public List<StationResponse> getAllStations();
    public StationResponse getStationById(Long id);
    public StationResponse getStationByCode(String code);
    public List<StationResponse> searchStations(String query);
    public StationResponse updateStation(Long id, StationRequest station); 
    public StationResponse updateStationStatus(Long id, boolean isActive);
}
