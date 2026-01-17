package com.example.vacationcalculator.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

/**
 * Response DTO for vacation pay calculation.
 * 
 * <p>Contains the calculated vacation pay amount, number of payable days,
 * and optional calculation details for transparency.</p>
 * 
 * <p>Uses Lombok {@code @Value} for immutability and builder pattern.
 * All monetary values use {@code BigDecimal} for precision.</p>
 */
@Value
@Builder
public class VacationPayResponse {

    /**
     * Calculated vacation pay amount in rubles.
     * Uses BigDecimal for precise monetary calculations.
     */
    BigDecimal vacationPay;

    /**
     * Number of payable vacation days used in calculation.
     * This excludes non-working holidays if dates were provided.
     */
    Integer payableDays;

    /**
     * Human-readable details about the calculation.
     * May include information about excluded holidays or calculation method.
     * Example: "Based on provided dates, excluding 2 holidays"
     */
    String calculationDetails;
}
