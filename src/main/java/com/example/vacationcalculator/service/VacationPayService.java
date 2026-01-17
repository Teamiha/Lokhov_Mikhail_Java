package com.example.vacationcalculator.service;

import com.example.vacationcalculator.dto.VacationPayRequest;
import com.example.vacationcalculator.dto.VacationPayResponse;
import com.example.vacationcalculator.model.HolidayCalendar;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Service for calculating vacation pay according to Russian labor law conventions.
 * 
 * <p>Implements the standard Russian vacation pay calculation formula:
 * <ul>
 *   <li>Average daily earnings = averageSalary / 29.3</li>
 *   <li>Vacation pay = average daily earnings × payable vacation days</li>
 * </ul>
 * </p>
 * 
 * <p>Payable vacation days are calculated as:
 * <ul>
 *   <li>If vacationDays provided: use that number directly</li>
 *   <li>If dates provided: calendar days between startDate and endDate (inclusive)
 *       minus official non-working holidays</li>
 * </ul>
 * </p>
 */
@Service
public class VacationPayService {

    /**
     * Official average monthly number of calendar days per Russian Ministry of Labor.
     * Used for calculating average daily earnings.
     */
    private static final BigDecimal AVERAGE_MONTHLY_DAYS = new BigDecimal("29.3");

    private static final int MONETARY_SCALE = 2;
    private static final RoundingMode MONETARY_ROUNDING = RoundingMode.HALF_UP;

    private final HolidayCalendar holidayCalendar;

    /**
     * Constructor for dependency injection.
     * 
     * @param holidayCalendar utility for counting Russian holidays
     */
    public VacationPayService(HolidayCalendar holidayCalendar) {
        this.holidayCalendar = holidayCalendar;
    }

    /**
     * Calculates vacation pay based on the provided request.
     * 
     * @param request vacation pay calculation request
     * @return response containing calculated vacation pay, payable days, and details
     * @throws IllegalArgumentException if request is invalid
     */
    public VacationPayResponse calculatePay(VacationPayRequest request) {
        validateRequest(request);

        BigDecimal averageDailyEarnings = calculateAverageDailyEarnings(request.getAverageSalary());
        int payableDays = calculatePayableDays(request);
        BigDecimal vacationPay = calculateVacationPay(averageDailyEarnings, payableDays);
        String calculationDetails = buildCalculationDetails(request, payableDays);

        return VacationPayResponse.builder()
                .vacationPay(vacationPay)
                .payableDays(payableDays)
                .calculationDetails(calculationDetails)
                .build();
    }

    /**
     * Calculates average daily earnings from monthly salary.
     * Formula: averageSalary / 29.3
     * 
     * @param averageSalary average monthly salary
     * @return average daily earnings rounded to 2 decimal places
     */
    private BigDecimal calculateAverageDailyEarnings(BigDecimal averageSalary) {
        return averageSalary.divide(AVERAGE_MONTHLY_DAYS, MONETARY_SCALE, MONETARY_ROUNDING);
    }

    /**
     * Calculates the number of payable vacation days.
     * 
     * @param request vacation pay request
     * @return number of payable vacation days
     */
    private int calculatePayableDays(VacationPayRequest request) {
        if (request.getVacationDays() != null) {
            return request.getVacationDays();
        }

        // Calculate days from date range, excluding holidays
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        // Calculate total calendar days (inclusive of both start and end dates)
        long totalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        // Subtract holidays falling within the period
        int holidays = holidayCalendar.countHolidaysBetween(startDate, endDate);

        return (int) (totalDays - holidays);
    }

    /**
     * Calculates vacation pay amount.
     * Formula: average daily earnings × payable vacation days
     * 
     * @param averageDailyEarnings average daily earnings
     * @param payableDays number of payable vacation days
     * @return vacation pay amount rounded to 2 decimal places
     */
    private BigDecimal calculateVacationPay(BigDecimal averageDailyEarnings, int payableDays) {
        return averageDailyEarnings
                .multiply(new BigDecimal(payableDays))
                .setScale(MONETARY_SCALE, MONETARY_ROUNDING);
    }

    /**
     * Builds human-readable calculation details.
     * 
     * @param request original request
     * @param payableDays calculated payable days
     * @return calculation details string
     */
    private String buildCalculationDetails(VacationPayRequest request, int payableDays) {
        if (request.getVacationDays() != null) {
            return String.format("Based on %d vacation days", request.getVacationDays());
        }

        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();
        long totalCalendarDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        int holidays = holidayCalendar.countHolidaysBetween(startDate, endDate);

        if (holidays > 0) {
            return String.format("Based on provided dates (%s to %s), %d calendar days excluding %d holiday(s)",
                    startDate, endDate, totalCalendarDays, holidays);
        } else {
            return String.format("Based on provided dates (%s to %s), %d calendar days",
                    startDate, endDate, totalCalendarDays);
        }
    }

    /**
     * Validates the request for business logic constraints.
     * 
     * @param request request to validate
     * @throws IllegalArgumentException if request is invalid
     */
    private void validateRequest(VacationPayRequest request) {
        if (!request.isValidRequest()) {
            throw new IllegalArgumentException(
                    "Either vacationDays or both startDate and endDate must be provided");
        }

        if (request.getStartDate() != null && request.getEndDate() != null) {
            if (request.getStartDate().isAfter(request.getEndDate())) {
                throw new IllegalArgumentException(
                        "Start date must be before or equal to end date");
            }
        }
    }
}
