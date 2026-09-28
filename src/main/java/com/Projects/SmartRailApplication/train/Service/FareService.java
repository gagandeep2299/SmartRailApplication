package com.Projects.SmartRailApplication.train.Service;

import com.Projects.SmartRailApplication.train.dto.FareRequest;
import com.Projects.SmartRailApplication.train.dto.FareResponse;

public interface FareService {
    public FareResponse createFare(FareRequest fare);
}
