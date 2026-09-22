package com.Projects.SmartRailApplication.train.dto;

import com.Projects.SmartRailApplication.train.Entities.enums.TrainStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class TrainResponse {
    private Long id;
    private String trainNumber;    
    private String name;
    private TrainStatus status;
}
