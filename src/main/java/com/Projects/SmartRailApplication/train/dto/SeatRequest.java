package com.Projects.SmartRailApplication.train.dto;

import com.Projects.SmartRailApplication.train.Entities.enums.SeatType;

import lombok.Data;

@Data 
public class SeatRequest {
    private Long id;
    private Long coachId;
    private String seatNumber;
    private SeatType seatType;
    private String seatStatus;
}
