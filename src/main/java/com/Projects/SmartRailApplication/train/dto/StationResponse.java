package com.Projects.SmartRailApplication.train.dto;

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
public class StationResponse {
    private String name;
    private String state;
    private String city;
    private String code;
    private boolean isActive;
}
