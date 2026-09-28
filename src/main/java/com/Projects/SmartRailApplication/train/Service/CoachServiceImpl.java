package com.Projects.SmartRailApplication.train.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Projects.SmartRailApplication.train.dto.CoachRequest;
import com.Projects.SmartRailApplication.train.dto.CoachResponse;

import jakarta.persistence.EntityNotFoundException;

import com.Projects.SmartRailApplication.train.Entities.Coach;
import com.Projects.SmartRailApplication.train.Entities.train;
import com.Projects.SmartRailApplication.train.Repository.coachRepository;
import com.Projects.SmartRailApplication.train.Repository.trainRepository;

@Service 
public class CoachServiceImpl implements CoachService {

    @Autowired
    private coachRepository coachRepository;

    @Autowired
    private  trainRepository trainRepository;

    @Override
    public CoachResponse addCoach(Long trainId, CoachRequest coachRequest) {
        if (coachRepository.existsByTrainIdAndId(trainId, coachRequest.getId())) {
            throw new IllegalArgumentException("Coach with ID " + coachRequest.getId() + " already exists for train with ID " + trainId);
        }
        train train = trainRepository.findById(trainId)
                .orElseThrow(() -> new EntityNotFoundException("Train with ID " + trainId + " does not exist"));

        Coach coach = new Coach();
        coach.setCoachNumber(coachRequest.getCoachNumber());
        coach.setTrainId(train);
        coach.setCoachType(coachRequest.getCoachType());
        return mapToResponse(coachRepository.save(coach));
    }

    @Override
    public List<CoachResponse> getCoachesByTrainId(Long trainId) {
        trainRepository.findById(trainId)
                .orElseThrow(() -> new EntityNotFoundException("Train with ID " + trainId + " does not exist"));
        return coachRepository.findByTrainId(trainId)
        .stream()
                .map(this::mapToResponse)
                .toList();
    }
    @Override
    public CoachResponse getCoachById(Long coachId) {
        Coach coach = coachRepository.findById(coachId)
                .orElseThrow(() -> new EntityNotFoundException("Coach with ID " + coachId + " does not exist"));
        return mapToResponse(coach);
    }
    @Override
    public CoachResponse updateCoach(Long coachId, CoachRequest coachRequest) {
        Coach coach = coachRepository.findById(coachId)
                .orElseThrow(() -> new EntityNotFoundException("Coach with ID " + coachId + " does not exist"));
        if (coachRequest.getCoachNumber() != null) {
            coach.setCoachNumber(coachRequest.getCoachNumber());
        }
        if (coachRequest.getCoachType() != null) {
            coach.setCoachType(coachRequest.getCoachType());
        }
        return mapToResponse(coachRepository.save(coach));
    }

    @Override
    public void deleteCoach(Long coachId) {
        Coach coach = coachRepository.findById(coachId)
                .orElseThrow(() -> new EntityNotFoundException("Coach with ID " + coachId + " does not exist"));
        coachRepository.delete(coach);
    }


    //helper function to map Coach entity to CoachResponse DTO
    private CoachResponse mapToResponse(Coach coach) {
        return  CoachResponse.builder()
                .id(coach.getId())
                .trainId(coach.getTrainId().getId())
                .coachNumber(coach.getCoachNumber())
                .build();
    }
    
}
