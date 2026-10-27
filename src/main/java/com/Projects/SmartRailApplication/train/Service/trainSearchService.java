package com.Projects.SmartRailApplication.train.Service;

import java.time.LocalDate;
import java.util.List;

import com.Projects.SmartRailApplication.train.Entities.Seat;
import com.Projects.SmartRailApplication.train.dto.TrainSearchResponse;

public interface trainSearchService {
    public List<TrainSearchResponse> searchTrains(String sourceStationCode, String destinationStationCode, LocalDate date);
    public List<Seat> isTrainAvailable(Long trainId, LocalDate travelDate, Long fromStopSequence, Long toStopSequence);
}
