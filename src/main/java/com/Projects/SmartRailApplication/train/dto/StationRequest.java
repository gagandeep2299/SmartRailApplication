package com.Projects.SmartRailApplication.train.dto;

import lombok.Data;

@Data 
public class StationRequest {
    private String name;
    private String state;
    private String city;
    private String code;
}
