package com.Projects.SmartRailApplication.train.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.Projects.SmartRailApplication.train.Entities.train;
import com.Projects.SmartRailApplication.train.Entities.enums.TrainStatus;
import com.Projects.SmartRailApplication.train.dto.TrainRequest;
import com.Projects.SmartRailApplication.train.dto.TrainResponse;

import jakarta.persistence.EntityNotFoundException;

import com.Projects.SmartRailApplication.train.Repository.trainRepository;

public class TrainServiceImpl implements TrainService {

    @Autowired
    private  trainRepository trainRepository;

    @Override
    public void addTrain(TrainRequest trainRequest) {
        if(trainRepository.existsByTrainNumber(trainRequest.getTrainNumber())) {
            throw new IllegalArgumentException("Train with this number already exists");
        }
        train train = new train();
        train.setName(trainRequest.getName());
        train.setTrainNumber(trainRequest.getTrainNumber());
        train.setStatus(trainRequest.getStatus());
        trainRepository.save(train);
    }

    @Override
    public List<TrainResponse> getAllTrains() {
        List<train> trains = trainRepository.findAll();
        return trains.stream()
                .map(train -> new TrainResponse(train.getId(), train.getName(), train.getTrainNumber(), train.getStatus()))
                .toList();
    }
    @Override
    public TrainResponse getTrainById(Long trainId) {
        train train = trainRepository.findById(trainId)
                .orElseThrow(() -> new EntityNotFoundException("Train not found with ID: " + trainId));
        return new TrainResponse(train.getId(), train.getName(), train.getTrainNumber(), train.getStatus());
    }
    @Override
    public TrainResponse getTrainByNumber(String trainNumber) {
        Optional<train> train = trainRepository.findByTrainNumber(trainNumber);
        if (train.isEmpty()) {
            throw new EntityNotFoundException("Train not found with number: " + trainNumber);
        }
        return new TrainResponse(train.get().getId(), train.get().getName(), train.get().getTrainNumber(), train.get().getStatus());
    }
    @Override
    public List<TrainResponse> getTrainsByName(String name) {
        List<train> trains = trainRepository.findByNameContainingIgnoreCase(name);
        if(trains.isEmpty()) {
            throw new EntityNotFoundException("No trains found with name containing: " + name);
        }
        return trains.stream()
                .map(train -> new TrainResponse(train.getId(), train.getName(), train.getTrainNumber(), train.getStatus()))
                .toList();
    }   

    @Override
    public void updateTrain(Long trainId, TrainRequest trainRequest) {
        train train = trainRepository.findById(trainId)
                .orElseThrow(() -> new EntityNotFoundException("Train not found with ID: " + trainId));
        train.setName(trainRequest.getName());
        train.setTrainNumber(trainRequest.getTrainNumber());
        train.setStatus(trainRequest.getStatus());
        trainRepository.save(train);
    }

    @Override
    public void updateTrainStatus(Long trainId, String status) {
        train train = trainRepository.findById(trainId)
            .orElseThrow(() -> new EntityNotFoundException("Train not found with ID: " + trainId));
        try{
            train.setStatus(TrainStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value: " + status);
        }
        trainRepository.save(train);
    }
    
    
}
