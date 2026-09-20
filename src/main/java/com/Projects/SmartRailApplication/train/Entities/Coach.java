package com.Projects.SmartRailApplication.train.Entities;

import com.Projects.SmartRailApplication.train.Entities.enums.CoachType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table (name = "coaches")
public class Coach {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "train_id")
    private train trainId;

    @Column (unique = true)
    private String coachNumber;

    @Enumerated (EnumType.STRING)
    private CoachType coachType;
}
