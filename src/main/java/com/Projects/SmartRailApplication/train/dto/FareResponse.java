package com.Projects.SmartRailApplication.train.dto;

import java.math.BigDecimal;

import com.Projects.SmartRailApplication.train.Entities.enums.CoachType;

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
public class FareResponse {
    private Long id;
    private Long trainId;
    private CoachType coachType;
    private Long sourceStationid;
    private Long destinationStationid;
    private BigDecimal fareAmount;
}