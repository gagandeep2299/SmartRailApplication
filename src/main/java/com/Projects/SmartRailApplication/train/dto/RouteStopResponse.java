package com.Projects.SmartRailApplication.train.dto;

import java.time.LocalTime;

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
public class RouteStopResponse {
    private Long id;
    private Long trainId;
    private Long stationId;
    private LocalTime arrivalTime;
    private LocalTime departureTime;
}