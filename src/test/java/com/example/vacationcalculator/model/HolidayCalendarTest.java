package com.example.vacationcalculator.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for HolidayCalendar.
 */
@DisplayName("HolidayCalendar Tests")
class HolidayCalendarTest {

    private HolidayCalendar holidayCalendar;

    @BeforeEach
    void setUp() {
        holidayCalendar = new HolidayCalendar();
    }

    @Test
    @DisplayName("Should recognize New Year holidays (January 1-8)")
    void testNewYearHolidays() {
        // Given
        LocalDate jan1 = LocalDate.of(2024, 1, 1);
        LocalDate jan8 = LocalDate.of(2024, 1, 8);
        LocalDate jan9 = LocalDate.of(2024, 1, 9);

        // When/Then
        assertThat(holidayCalendar.isHoliday(jan1)).isTrue();
        assertThat(holidayCalendar.isHoliday(jan8)).isTrue();
        assertThat(holidayCalendar.isHoliday(jan9)).isFalse();
    }

    @Test
    @DisplayName("Should recognize Defender of the Fatherland Day (February 23)")
    void testDefenderOfTheFatherlandDay() {
        // Given
        LocalDate feb23 = LocalDate.of(2024, 2, 23);
        LocalDate feb22 = LocalDate.of(2024, 2, 22);

        // When/Then
        assertThat(holidayCalendar.isHoliday(feb23)).isTrue();
        assertThat(holidayCalendar.isHoliday(feb22)).isFalse();
    }

    @Test
    @DisplayName("Should recognize International Women's Day (March 8)")
    void testWomensDay() {
        // Given
        LocalDate mar8 = LocalDate.of(2024, 3, 8);
        LocalDate mar7 = LocalDate.of(2024, 3, 7);

        // When/Then
        assertThat(holidayCalendar.isHoliday(mar8)).isTrue();
        assertThat(holidayCalendar.isHoliday(mar7)).isFalse();
    }

    @Test
    @DisplayName("Should recognize Spring and Labor Day (May 1)")
    void testLaborDay() {
        // Given
        LocalDate may1 = LocalDate.of(2024, 5, 1);
        LocalDate may2 = LocalDate.of(2024, 5, 2);

        // When/Then
        assertThat(holidayCalendar.isHoliday(may1)).isTrue();
        assertThat(holidayCalendar.isHoliday(may2)).isFalse();
    }

    @Test
    @DisplayName("Should recognize Victory Day (May 9)")
    void testVictoryDay() {
        // Given
        LocalDate may9 = LocalDate.of(2024, 5, 9);
        LocalDate may10 = LocalDate.of(2024, 5, 10);

        // When/Then
        assertThat(holidayCalendar.isHoliday(may9)).isTrue();
        assertThat(holidayCalendar.isHoliday(may10)).isFalse();
    }

    @Test
    @DisplayName("Should recognize Russia Day (June 12)")
    void testRussiaDay() {
        // Given
        LocalDate jun12 = LocalDate.of(2024, 6, 12);
        LocalDate jun11 = LocalDate.of(2024, 6, 11);

        // When/Then
        assertThat(holidayCalendar.isHoliday(jun12)).isTrue();
        assertThat(holidayCalendar.isHoliday(jun11)).isFalse();
    }

    @Test
    @DisplayName("Should recognize Unity Day (November 4)")
    void testUnityDay() {
        // Given
        LocalDate nov4 = LocalDate.of(2024, 11, 4);
        LocalDate nov3 = LocalDate.of(2024, 11, 3);

        // When/Then
        assertThat(holidayCalendar.isHoliday(nov4)).isTrue();
        assertThat(holidayCalendar.isHoliday(nov3)).isFalse();
    }

    @Test
    @DisplayName("Should count holidays in date range")
    void testCountHolidaysBetween() {
        // Given
        LocalDate start = LocalDate.of(2024, 5, 1); // May 1 is a holiday
        LocalDate end = LocalDate.of(2024, 5, 10); // May 9 is also a holiday

        // When
        int holidayCount = holidayCalendar.countHolidaysBetween(start, end);

        // Then
        assertThat(holidayCount).isEqualTo(2); // May 1 and May 9
    }

    @Test
    @DisplayName("Should count zero holidays when none in range")
    void testCountHolidaysBetweenNoHolidays() {
        // Given
        LocalDate start = LocalDate.of(2024, 6, 10);
        LocalDate end = LocalDate.of(2024, 6, 20);

        // When
        int holidayCount = holidayCalendar.countHolidaysBetween(start, end);

        // Then
        assertThat(holidayCount).isEqualTo(0);
    }

    @Test
    @DisplayName("Should count holidays for single day range")
    void testCountHolidaysBetweenSingleDay() {
        // Given
        LocalDate date = LocalDate.of(2024, 5, 1); // May 1 is a holiday

        // When
        int holidayCount = holidayCalendar.countHolidaysBetween(date, date);

        // Then
        assertThat(holidayCount).isEqualTo(1);
    }

    @Test
    @DisplayName("Should count all New Year holidays in range")
    void testCountNewYearHolidays() {
        // Given
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 8);

        // When
        int holidayCount = holidayCalendar.countHolidaysBetween(start, end);

        // Then
        assertThat(holidayCount).isEqualTo(8); // January 1-8
    }

    @Test
    @DisplayName("Should throw exception when start date is after end date")
    void testCountHolidaysBetweenInvalidRange() {
        // Given
        LocalDate start = LocalDate.of(2024, 6, 10);
        LocalDate end = LocalDate.of(2024, 6, 1);

        // When/Then
        assertThatThrownBy(() -> holidayCalendar.countHolidaysBetween(start, end))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Start date must be before or equal to end date");
    }

    @Test
    @DisplayName("Should work across different years")
    void testHolidaysAcrossYears() {
        // Given
        LocalDate jan1_2024 = LocalDate.of(2024, 1, 1);
        LocalDate jan1_2025 = LocalDate.of(2025, 1, 1);
        LocalDate jan1_2026 = LocalDate.of(2026, 1, 1);

        // When/Then
        assertThat(holidayCalendar.isHoliday(jan1_2024)).isTrue();
        assertThat(holidayCalendar.isHoliday(jan1_2025)).isTrue();
        assertThat(holidayCalendar.isHoliday(jan1_2026)).isTrue();
    }
}
