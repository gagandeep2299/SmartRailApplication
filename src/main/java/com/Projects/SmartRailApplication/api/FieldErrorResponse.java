package com.Projects.SmartRailApplication.api;

public record FieldErrorResponse(
    String field,
    String message
) {
    
}
