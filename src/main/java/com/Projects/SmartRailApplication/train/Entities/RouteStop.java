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

@Entity 
@Table (name ="route_stop")
public class RouteStop {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "train_id")
    private train trainId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "station_id")
    private Station stationId;

    private LocalTime arrivalTime;

    private LocalTime departureTime;
    
}
