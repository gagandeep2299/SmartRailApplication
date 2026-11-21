package com.Projects.SmartRailApplication.api;

import java.time.Instant;
import java.util.List;

public record ApiErrorResponse(
    Instant timestamp,
    int status,
    String error,
    String code,
    String message,
    String Path,
    List<FieldErrorResponse> fieldErrors
) {
    
}
