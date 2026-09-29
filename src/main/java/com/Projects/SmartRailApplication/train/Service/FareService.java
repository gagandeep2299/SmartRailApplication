package com.Projects.SmartRailApplication.train.Service;

import com.Projects.SmartRailApplication.train.Entities.enums.CoachType;
import com.Projects.SmartRailApplication.train.dto.FareRequest;
import com.Projects.SmartRailApplication.train.dto.FareResponse;

public interface FareService {
    public FareResponse createFare(FareRequest fare);
    public FareResponse getFare(Long trainId, Long sourceStationId, Long destinationStationId, CoachType coachType);
    public FareResponse getFareById(Long id);
    public FareResponse updateFare(Long id, FareRequest fareRequest);
    public void deleteFare(Long id);
}
