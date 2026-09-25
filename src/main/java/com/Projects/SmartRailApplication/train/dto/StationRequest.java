package com.Projects.SmartRailApplication.train.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder 
public class StationRequest {
    private String name;
    private String state;
    private String city;
    private String code;
    private boolean isActive;
}
