package com.Projects.SmartRailApplication.booking.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data 
@Getter 
@Setter 
@NoArgsConstructor 
public class bookingRequest {

    @NotNull 
    private Long train_id;
    @NotNull
    private Long source_station_id;
    @NotNull
    private Long destination_station_id;

    @NotNull
    @FutureOrPresent 
    private LocalDate travelDateTime;

    @NotEmpty 
    private List<@NotNull Long> seatIds;
}
