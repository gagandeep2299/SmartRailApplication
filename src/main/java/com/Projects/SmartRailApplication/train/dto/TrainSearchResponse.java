package com.Projects.SmartRailApplication.train.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.Projects.SmartRailApplication.train.Entities.Coach;
import com.Projects.SmartRailApplication.train.Entities.enums.TrainStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@Builder 
@AllArgsConstructor 
public class TrainSearchResponse {
    private Long id;
    private String trainNumber;    
    private String trainName;
    private TrainStatus trainStatus;
    private String sourceStationName;
    private String sourceStationState;
    private String sourceStationCity;
    private String sourceStationCode;
    private LocalTime departureTime;
    private String destinationStationName;
    private String destinationStationState;
    private String destinationStationCity;
    private String destinationStationCode;
    private LocalTime ArrivalTime;
    private List<CoachAvailabilityResponse> coaches;

}
