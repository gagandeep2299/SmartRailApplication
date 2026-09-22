package com.Projects.SmartRailApplication.train.dto;

import java.math.BigDecimal;

import com.Projects.SmartRailApplication.train.Entities.enums.CoachType;

import lombok.Data;

@Data
public class FareRequest {
    private Long id;
    private Long trainId;
    private CoachType coachType;
    private Long sourceStationid;
    private Long destinationStationid;
    private BigDecimal fareAmount;
}