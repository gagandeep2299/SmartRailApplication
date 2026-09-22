package com.Projects.SmartRailApplication.train.dto;

import com.Projects.SmartRailApplication.train.Entities.enums.TrainStatus;

import lombok.Data;

@Data 
public class TrainRequest {
    private Long id;
    private String trainNumber;    
    private String name;
    private TrainStatus status;
}
