package com.Projects.SmartRailApplication.train.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor  
public class StationRequest {
    private String name;
    private String state;
    private String city;
    private String code;
    private boolean isActive;
}
