package com.example.vacationcalculator.exception;

/**
 * Exception thrown when vacation pay calculation request is invalid.
 * 
 * <p>This exception is used for business logic validation errors,
 * such as invalid date ranges or missing required parameters.</p>
 */
public class InvalidVacationRequestException extends RuntimeException {

    /**
     * Constructs a new InvalidVacationRequestException with the specified detail message.
     * 
     * @param message the detail message
     */
    public InvalidVacationRequestException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidVacationRequestException with the specified detail message
     * and cause.
     * 
     * @param message the detail message
     * @param cause the cause
     */
    public InvalidVacationRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
