package com.Projects.SmartRailApplication.train.dto;

import java.time.LocalTime;

import lombok.Data;

@Data
public class RouteStopRequest {
    private Long id;
    private Long trainId;
    private Long stationId;
    private LocalTime arrivalTime;
    private LocalTime departureTime;
}