package com.example.vacationcalculator.service;

import com.example.vacationcalculator.dto.VacationPayRequest;
import com.example.vacationcalculator.dto.VacationPayResponse;
import com.example.vacationcalculator.exception.InvalidVacationRequestException;
import com.example.vacationcalculator.model.HolidayCalendar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

/**
 * Unit tests for VacationPayService.
 * 
 * <p>Tests cover:
 * <ul>
 *   <li>Basic calculation with vacationDays</li>
 *   <li>Calculation with dates, no holidays</li>
 *   <li>Calculation with dates, excluding holidays</li>
 *   <li>Edge cases and validation</li>
 * </ul>
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("VacationPayService Tests")
class VacationPayServiceTest {

    @Mock
    private HolidayCalendar holidayCalendar;

    private VacationPayService vacationPayService;

    @BeforeEach
    void setUp() {
        vacationPayService = new VacationPayService(holidayCalendar);
    }

    @Test
    @DisplayName("Should calculate vacation pay with vacationDays")
    void testCalculatePayWithVacationDays() {
        // Given
        BigDecimal averageSalary = new BigDecimal("100000");
        Integer vacationDays = 28;
        VacationPayRequest request = VacationPayRequest.builder()
                .averageSalary(averageSalary)
                .vacationDays(vacationDays)
                .build();

        // When
        VacationPayResponse response = vacationPayService.calculatePay(request);

        // Then
        // Expected: 100000 / 29.3 = 3412.97 (rounded), then 3412.97 * 28 = 95563.16
        assertThat(response.getVacationPay()).isEqualByComparingTo(new BigDecimal("95563.16"));
        assertThat(response.getPayableDays()).isEqualTo(28);
        assertThat(response.getCalculationDetails()).isEqualTo("Based on 28 vacation days");
    }

    @Test
    @DisplayName("Should calculate vacation pay with dates, no holidays")
    void testCalculatePayWithDatesNoHolidays() {
        // Given
        BigDecimal averageSalary = new BigDecimal("100000");
        LocalDate startDate = LocalDate.of(2024, 6, 10);
        LocalDate endDate = LocalDate.of(2024, 7, 7); // 28 days inclusive
        VacationPayRequest request = VacationPayRequest.builder()
                .averageSalary(averageSalary)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        when(holidayCalendar.countHolidaysBetween(startDate, endDate)).thenReturn(0);

        // When
        VacationPayResponse response = vacationPayService.calculatePay(request);

        // Then
        // Expected: 100000 / 29.3 = 3412.97 (rounded), then 3412.97 * 28 = 95563.16
        assertThat(response.getVacationPay()).isEqualByComparingTo(new BigDecimal("95563.16"));
        assertThat(response.getPayableDays()).isEqualTo(28);
        assertThat(response.getCalculationDetails())
                .contains("Based on provided dates")
                .contains("28 calendar days");
    }

    @Test
    @DisplayName("Should calculate vacation pay with dates, excluding holidays")
    void testCalculatePayWithDatesExcludingHolidays() {
        // Given
        BigDecimal averageSalary = new BigDecimal("100000");
        LocalDate startDate = LocalDate.of(2024, 5, 1); // May 1 is a holiday
        LocalDate endDate = LocalDate.of(2024, 5, 10); // 10 days inclusive
        VacationPayRequest request = VacationPayRequest.builder()
                .averageSalary(averageSalary)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        // May 1 and May 9 are holidays in this range
        when(holidayCalendar.countHolidaysBetween(startDate, endDate)).thenReturn(2);

        // When
        VacationPayResponse response = vacationPayService.calculatePay(request);

        // Then
        // Expected: 100000 / 29.3 = 3412.97 (rounded), then 3412.97 * 8 = 27303.76
        assertThat(response.getVacationPay()).isEqualByComparingTo(new BigDecimal("27303.76"));
        assertThat(response.getPayableDays()).isEqualTo(8); // 10 days - 2 holidays
        assertThat(response.getCalculationDetails())
                .contains("Based on provided dates")
                .contains("10 calendar days")
                .contains("excluding 2 holiday(s)");
    }

    @Test
    @DisplayName("Should calculate vacation pay for single day")
    void testCalculatePaySingleDay() {
        // Given
        BigDecimal averageSalary = new BigDecimal("100000");
        LocalDate date = LocalDate.of(2024, 6, 15);
        VacationPayRequest request = VacationPayRequest.builder()
                .averageSalary(averageSalary)
                .startDate(date)
                .endDate(date)
                .build();

        when(holidayCalendar.countHolidaysBetween(date, date)).thenReturn(0);

        // When
        VacationPayResponse response = vacationPayService.calculatePay(request);

        // Then
        // Expected: 100000 / 29.3 * 1 = 3412.97
        assertThat(response.getVacationPay()).isEqualByComparingTo(new BigDecimal("3412.97"));
        assertThat(response.getPayableDays()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should handle zero salary")
    void testCalculatePayWithZeroSalary() {
        // Given
        BigDecimal averageSalary = BigDecimal.ZERO;
        Integer vacationDays = 28;
        VacationPayRequest request = VacationPayRequest.builder()
                .averageSalary(averageSalary)
                .vacationDays(vacationDays)
                .build();

        // When
        VacationPayResponse response = vacationPayService.calculatePay(request);

        // Then
        assertThat(response.getVacationPay()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(response.getPayableDays()).isEqualTo(28);
    }

    @Test
    @DisplayName("Should handle very small salary")
    void testCalculatePayWithVerySmallSalary() {
        // Given
        BigDecimal averageSalary = new BigDecimal("0.01");
        Integer vacationDays = 1;
        VacationPayRequest request = VacationPayRequest.builder()
                .averageSalary(averageSalary)
                .vacationDays(vacationDays)
                .build();

        // When
        VacationPayResponse response = vacationPayService.calculatePay(request);

        // Then
        // Expected: 0.01 / 29.3 * 1 = 0.00034... rounded to 0.00
        assertThat(response.getVacationPay()).isEqualByComparingTo(new BigDecimal("0.00"));
        assertThat(response.getPayableDays()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should handle large salary")
    void testCalculatePayWithLargeSalary() {
        // Given
        BigDecimal averageSalary = new BigDecimal("500000");
        Integer vacationDays = 14;
        VacationPayRequest request = VacationPayRequest.builder()
                .averageSalary(averageSalary)
                .vacationDays(vacationDays)
                .build();

        // When
        VacationPayResponse response = vacationPayService.calculatePay(request);

        // Then
        // Expected: 500000 / 29.3 = 17064.85 (rounded), then 17064.85 * 14 = 238907.90
        assertThat(response.getVacationPay()).isEqualByComparingTo(new BigDecimal("238907.90"));
        assertThat(response.getPayableDays()).isEqualTo(14);
    }

    @Test
    @DisplayName("Should round monetary values correctly")
    void testMonetaryRounding() {
        // Given
        BigDecimal averageSalary = new BigDecimal("100000");
        Integer vacationDays = 1;
        VacationPayRequest request = VacationPayRequest.builder()
                .averageSalary(averageSalary)
                .vacationDays(vacationDays)
                .build();

        // When
        VacationPayResponse response = vacationPayService.calculatePay(request);

        // Then
        // 100000 / 29.3 = 3412.9692832764505... rounded to 3412.97
        assertThat(response.getVacationPay()).isEqualByComparingTo(new BigDecimal("3412.97"));
        assertThat(response.getVacationPay().scale()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should throw exception when neither vacationDays nor dates provided")
    void testValidationNeitherVacationDaysNorDates() {
        // Given
        VacationPayRequest request = VacationPayRequest.builder()
                .averageSalary(new BigDecimal("100000"))
                .build();

        // When/Then
        assertThatThrownBy(() -> vacationPayService.calculatePay(request))
                .isInstanceOf(InvalidVacationRequestException.class)
                .hasMessageContaining("Either vacationDays or both startDate and endDate must be provided");
    }

    @Test
    @DisplayName("Should throw exception when only startDate provided")
    void testValidationOnlyStartDate() {
        // Given
        VacationPayRequest request = VacationPayRequest.builder()
                .averageSalary(new BigDecimal("100000"))
                .startDate(LocalDate.of(2024, 6, 1))
                .build();

        // When/Then
        assertThatThrownBy(() -> vacationPayService.calculatePay(request))
                .isInstanceOf(InvalidVacationRequestException.class)
                .hasMessageContaining("Either vacationDays or both startDate and endDate must be provided");
    }

    @Test
    @DisplayName("Should throw exception when only endDate provided")
    void testValidationOnlyEndDate() {
        // Given
        VacationPayRequest request = VacationPayRequest.builder()
                .averageSalary(new BigDecimal("100000"))
                .endDate(LocalDate.of(2024, 6, 30))
                .build();

        // When/Then
        assertThatThrownBy(() -> vacationPayService.calculatePay(request))
                .isInstanceOf(InvalidVacationRequestException.class)
                .hasMessageContaining("Either vacationDays or both startDate and endDate must be provided");
    }

    @Test
    @DisplayName("Should throw exception when startDate is after endDate")
    void testValidationStartDateAfterEndDate() {
        // Given
        VacationPayRequest request = VacationPayRequest.builder()
                .averageSalary(new BigDecimal("100000"))
                .startDate(LocalDate.of(2024, 6, 30))
                .endDate(LocalDate.of(2024, 6, 1))
                .build();

        // When/Then
        assertThatThrownBy(() -> vacationPayService.calculatePay(request))
                .isInstanceOf(InvalidVacationRequestException.class)
                .hasMessageContaining("Start date must be before or equal to end date");
    }

    @Test
    @DisplayName("Should throw exception when both vacationDays and dates provided")
    void testValidationBothVacationDaysAndDates() {
        // Given
        VacationPayRequest request = VacationPayRequest.builder()
                .averageSalary(new BigDecimal("100000"))
                .vacationDays(28)
                .startDate(LocalDate.of(2024, 6, 1))
                .endDate(LocalDate.of(2024, 6, 28))
                .build();

        // When/Then
        assertThatThrownBy(() -> vacationPayService.calculatePay(request))
                .isInstanceOf(InvalidVacationRequestException.class)
                .hasMessageContaining("Either vacationDays or both startDate and endDate must be provided");
    }

    @Test
    @DisplayName("Should handle date range spanning multiple months")
    void testCalculatePayDateRangeMultipleMonths() {
        // Given
        BigDecimal averageSalary = new BigDecimal("100000");
        LocalDate startDate = LocalDate.of(2024, 5, 25);
        LocalDate endDate = LocalDate.of(2024, 6, 10); // 17 days inclusive
        VacationPayRequest request = VacationPayRequest.builder()
                .averageSalary(averageSalary)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        when(holidayCalendar.countHolidaysBetween(startDate, endDate)).thenReturn(0);

        // When
        VacationPayResponse response = vacationPayService.calculatePay(request);

        // Then
        // Expected: 100000 / 29.3 = 3412.97 (rounded), then 3412.97 * 17 = 58020.49
        assertThat(response.getVacationPay()).isEqualByComparingTo(new BigDecimal("58020.49"));
        assertThat(response.getPayableDays()).isEqualTo(17);
    }

    @Test
    @DisplayName("Should handle all days in range being holidays")
    void testCalculatePayAllDaysAreHolidays() {
        // Given
        BigDecimal averageSalary = new BigDecimal("100000");
        LocalDate startDate = LocalDate.of(2024, 1, 1); // New Year holiday
        LocalDate endDate = LocalDate.of(2024, 1, 3); // 3 days inclusive
        VacationPayRequest request = VacationPayRequest.builder()
                .averageSalary(averageSalary)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        // All 3 days are holidays
        when(holidayCalendar.countHolidaysBetween(startDate, endDate)).thenReturn(3);

        // When
        VacationPayResponse response = vacationPayService.calculatePay(request);

        // Then
        assertThat(response.getVacationPay()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(response.getPayableDays()).isEqualTo(0); // 3 days - 3 holidays
    }
}
