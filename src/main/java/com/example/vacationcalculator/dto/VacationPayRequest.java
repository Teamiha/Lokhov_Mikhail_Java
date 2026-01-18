package com.example.vacationcalculator.dto;

import lombok.Builder;
import lombok.Value;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request DTO for vacation pay calculation.
 * 
 * <p>Either {@code vacationDays} or both {@code startDate} and {@code endDate}
 * must be provided (mutually exclusive options).</p>
 * 
 * <p>Uses Lombok {@code @Value} for immutability and builder pattern for convenient
 * object construction. All monetary values use {@code BigDecimal} for precision.</p>
 */
@Value
@Builder
public class VacationPayRequest {

    /**
     * Average monthly salary for the last 12 months.
     * Must be positive and greater than zero.
     */
    @NotNull(message = "Average salary is required")
    @DecimalMin(value = "0.01", message = "Average salary must be greater than 0")
    BigDecimal averageSalary;

    /**
     * Number of vacation days (used if dates not provided).
     * Must be positive if provided.
     */
    @Positive(message = "Vacation days must be positive")
    Integer vacationDays;

    /**
     * Vacation start date (format: YYYY-MM-DD).
     * Must be provided together with endDate if vacationDays is not provided.
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate startDate;

    /**
     * Vacation end date (format: YYYY-MM-DD).
     * Must be provided together with startDate if vacationDays is not provided.
     * Must be equal to or after startDate.
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate endDate;

    /**
     * Validates that either vacationDays or both dates are provided.
     * This is a business logic validation performed in the controller or service layer.
     * 
     * @return true if exactly one option (vacationDays OR dates) is provided
     */
    public boolean isValidRequest() {
        boolean hasVacationDays = vacationDays != null && vacationDays > 0;
        boolean hasDates = startDate != null && endDate != null;
        return hasVacationDays ^ hasDates; // XOR: exactly one option must be provided
    }
}
