package com.Projects.SmartRailApplication.train.Entities;


import java.math.BigDecimal;

import com.Projects.SmartRailApplication.train.Entities.enums.CoachType;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table (name ="fares")
public class Fare {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn (name = "train_id")
    private train trainId;

    @Enumerated (jakarta.persistence.EnumType.STRING)
    private CoachType coachType;

    @ManyToOne (fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn (name = "source_station_id")
    private Station sourceStationid;

    @ManyToOne (fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn (name = "destination_station_id")
    private Station destinationStationid;

    private BigDecimal fareAmount;
}
