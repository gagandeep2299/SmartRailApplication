package com.Projects.SmartRailApplication.train.Service;

import java.util.List;

import com.Projects.SmartRailApplication.train.dto.TrainRequest;
import com.Projects.SmartRailApplication.train.dto.TrainResponse;

public interface trainService {
    void addTrain(TrainRequest trainRequest);
    List<TrainResponse> getAllTrains();
    TrainResponse getTrainById(Long trainId);
    TrainResponse getTrainByNumber(String trainNumber);
    List<TrainResponse> getTrainsByName(String name);
    void updateTrain(Long trainId, TrainRequest trainRequest);
    void updateTrainStatus(Long trainId, String status);
}
