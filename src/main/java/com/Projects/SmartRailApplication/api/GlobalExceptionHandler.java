package com.Projects.SmartRailApplication.api;

import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.Projects.SmartRailApplication.api.exception.BusinessRuleViolationException;
import com.Projects.SmartRailApplication.api.exception.ResourceConflictException;
import com.Projects.SmartRailApplication.api.exception.ResourceNotFoundException;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice 
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    //Resource not found exception handler
    @ExceptionHandler ({
        ResourceNotFoundException.class, 
        EntityNotFoundException.class})
        public ResponseEntity<ApiErrorResponse> handleNotFound(Exception ex, HttpServletRequest request) {
            return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                "RESOURCE_NOT_FOUND",
                ex.getMessage(),
                request.getRequestURI(),
                List.of()
            );
        }
    
    // Resource Conflict - 409

    @ExceptionHandler ({
        ResourceConflictException.class,
            EntityExistsException.class,
            DataIntegrityViolationException.class}
    )
    public ResponseEntity<ApiErrorResponse> handleBusinessRuleViolation(
        Exception ex,
         HttpServletRequest request) {
        return buildErrorResponse(
             HttpStatus.CONFLICT,
                "RESOURCE_CONFLICT",
                "The requested operation conflicts with existing data.",
                request.getRequestURI(),
                List.of()
        );
    }

    // Business Rule Violation - 409
    @ExceptionHandler (BusinessRuleViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessRuleViolation(
        BusinessRuleViolationException ex, HttpServletRequest request) {
        return buildErrorResponse(
            HttpStatus.CONFLICT,
                "BUSINESS_RULE_VIOLATION",
                ex.getMessage(),
                request.getRequestURI(),
                List.of()
        );
    }

    // Iligal Argument Exception - 400
    @ExceptionHandler (IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(
        IllegalArgumentException ex, HttpServletRequest request) {
        return buildErrorResponse(
            HttpStatus.BAD_REQUEST,
                "ILLEGAL_ARGUMENT",
                ex.getMessage(),
                request.getRequestURI(),
                List.of()
        );
    }

    // Iligal State Exception - 409
    @ExceptionHandler (IllegalStateException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalStateException(
        IllegalStateException ex, HttpServletRequest request) {
        return buildErrorResponse(
            HttpStatus.CONFLICT,
                "INVALID_STATE",
                ex.getMessage(),
                request.getRequestURI(),
                List.of()
        );
    }

    // DTO validation errors - 400
    @ExceptionHandler (MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(
        MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<FieldErrorResponse> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
            .map(this::toFieldError)
            .toList();
        return buildErrorResponse(
            HttpStatus.BAD_REQUEST,
                "VALIDATION_FAILED",
                "Request validation failed.",
                request.getRequestURI(),
                fieldErrors
        );
    }

    // Constraint validation errors - 400
    @ExceptionHandler (ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolationException(
        ConstraintViolationException ex, HttpServletRequest request) {
        List<FieldErrorResponse> fieldErrors = ex.getConstraintViolations().stream()
            .map(violation -> new FieldErrorResponse(violation.getPropertyPath().toString(), violation.getMessage()))
            .toList();
        return buildErrorResponse(
            HttpStatus.BAD_REQUEST,
                "VALIDATION_FAILED",
                "Request validation failed.",
                request.getRequestURI(),
                fieldErrors
        );
    }

    // Malformed JSON - 400

    @ExceptionHandler (HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleMalformedJsonException(
        HttpMessageNotReadableException ex, HttpServletRequest request) {
        return buildErrorResponse(
             HttpStatus.BAD_REQUEST,
                "MALFORMED_JSON",
                "Request body is missing or contains invalid JSON.",
                request.getRequestURI(),
                List.of()
        );
    }

    // Missing request parameter - 400
    @ExceptionHandler (MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingRequestParameterException( 
        MissingServletRequestParameterException ex, HttpServletRequest request) {
        return buildErrorResponse(
            HttpStatus.BAD_REQUEST,
                "MISSING_PARAMETER",
                "Required parameter is missing: " + ex.getParameterName(),
                request.getRequestURI(),
                List.of()
        );
    }

    // Invalid path variable or parameter type - 400
    @ExceptionHandler (MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleNumberFormatException(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        return buildErrorResponse(
             HttpStatus.BAD_REQUEST,
                "INVALID_PARAMETER_TYPE",
                "Invalid value supplied for parameter: " + ex.getName(),
                request.getRequestURI(),
                List.of()
        );
    }

    // Unexpected exceptions - 500
    @ExceptionHandler (Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedException(Exception ex, HttpServletRequest request) {
        logger.error(
                "Unexpected error while processing request: {}",
                request.getRequestURI(),
                ex
        );
        return buildErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred. Please try again later.",
                request.getRequestURI(),
                List.of()
        );
    }


    private ResponseEntity<ApiErrorResponse> buildErrorResponse(
                        HttpStatus status,
                        String code, 
                        String message,
                        String path,
                        List<FieldErrorResponse> fieldErrors) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
            Instant.now(),
            status.value(),
            status.getReasonPhrase(),
            code,
            message,
            path,
            fieldErrors
        );
        return ResponseEntity.status(status).body(errorResponse);
    }
    private FieldErrorResponse toFieldError(FieldError error) {
        return new FieldErrorResponse(
                error.getField(),
                error.getDefaultMessage()
        );
    }
}
