package com.Projects.SmartRailApplication.train.dto;

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
public class CoachResponse {
    private Long id;
    private Long trainId;
    private String coachNumber;
    private CoachType coachType;
}