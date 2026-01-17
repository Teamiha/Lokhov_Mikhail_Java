package com.example.vacationcalculator.exception;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * Standard error response DTO for API error messages.
 * 
 * <p>Provides a consistent structure for error responses across the API.</p>
 */
@Value
@Builder
public class ErrorResponse {

    /**
     * HTTP status code.
     */
    int status;

    /**
     * Error message describing what went wrong.
     */
    String message;

    /**
     * Timestamp when the error occurred.
     */
    LocalDateTime timestamp;

    /**
     * Optional path where the error occurred.
     */
    String path;
}
