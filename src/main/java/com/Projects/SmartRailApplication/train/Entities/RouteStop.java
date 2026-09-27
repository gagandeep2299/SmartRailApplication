package com.Projects.SmartRailApplication.train.Entities;

import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity 
@Getter 
@Setter 
@Table (name ="route_stop")
public class RouteStop {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "train_id")
    private train train;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "station_id")
    private Station station;

    private Long stopSequence;

    private LocalTime arrivalTime;

    private LocalTime departureTime;

    
}
