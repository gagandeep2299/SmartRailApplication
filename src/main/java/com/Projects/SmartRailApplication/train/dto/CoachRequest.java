package com.Projects.SmartRailApplication.train.dto;

import com.Projects.SmartRailApplication.train.Entities.enums.CoachType;

import lombok.Data;

@Data
public class CoachRequest {
    private Long id;
    private Long trainId;
    private String coachNumber;
    private CoachType coachType;
}