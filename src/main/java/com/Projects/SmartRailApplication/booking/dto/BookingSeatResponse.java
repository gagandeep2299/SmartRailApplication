package com.Projects.SmartRailApplication.booking.dto;

import java.math.BigDecimal;

import com.Projects.SmartRailApplication.train.Entities.enums.CoachType;
import com.Projects.SmartRailApplication.train.Entities.enums.SeatType;

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
public class BookingSeatResponse {
    private Long bookingSeatId;
    private Long seatId;
    private String seatNumber;
    private SeatType seatType;

    private Long coachId;
    private String coachName;
    private CoachType coachType;

    private BigDecimal price;
}