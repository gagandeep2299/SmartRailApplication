package com.Projects.SmartRailApplication.api.exception;

public class BusinessRuleViolationException  extends RuntimeException {
    public BusinessRuleViolationException(String message) {
        super(message);
    }
    
}
