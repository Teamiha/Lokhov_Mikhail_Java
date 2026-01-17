package com.example.vacationcalculator.model;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for managing Russian non-working holidays.
 * 
 * <p>Contains hardcoded set of official Russian holidays. Fixed holidays are dates
 * that always occur on the same calendar day, regardless of weekends
 * (though weekends may extend the holiday period).</p>
 * 
 * <p>Fixed holidays in Russia:
 * <ul>
 *   <li>January 1-8: New Year holidays</li>
 *   <li>February 23: Defender of the Fatherland Day</li>
 *   <li>March 8: International Women's Day</li>
 *   <li>May 1: Spring and Labor Day</li>
 *   <li>May 9: Victory Day</li>
 *   <li>June 12: Russia Day</li>
 *   <li>November 4: Unity Day</li>
 * </ul>
 * </p>
 * 
 * <p>This implementation uses {@code MonthDay} to represent fixed holidays,
 * which allows the same holidays to be recognized across all years without
 * needing to hardcode specific dates for each year.</p>
 */
@Component
public class HolidayCalendar {

    private static final Set<MonthDay> FIXED_HOLIDAYS = new HashSet<>();

    static {
        // New Year holidays (January 1-8)
        for (int day = 1; day <= 8; day++) {
            FIXED_HOLIDAYS.add(MonthDay.of(Month.JANUARY, day));
        }
        // February 23 - Defender of the Fatherland Day
        FIXED_HOLIDAYS.add(MonthDay.of(Month.FEBRUARY, 23));
        // March 8 - International Women's Day
        FIXED_HOLIDAYS.add(MonthDay.of(Month.MARCH, 8));
        // May 1 - Spring and Labor Day
        FIXED_HOLIDAYS.add(MonthDay.of(Month.MAY, 1));
        // May 9 - Victory Day
        FIXED_HOLIDAYS.add(MonthDay.of(Month.MAY, 9));
        // June 12 - Russia Day
        FIXED_HOLIDAYS.add(MonthDay.of(Month.JUNE, 12));
        // November 4 - Unity Day
        FIXED_HOLIDAYS.add(MonthDay.of(Month.NOVEMBER, 4));
    }

    /**
     * Counts the number of official non-working holidays between two dates (inclusive).
     * 
     * <p>Iterates through all dates in the range and checks if each date falls on
     * a fixed holiday. This method works for any year since it uses MonthDay
     * to match holidays.</p>
     * 
     * @param start start date (inclusive)
     * @param end end date (inclusive)
     * @return number of holidays falling within the period
     * @throws IllegalArgumentException if start date is after end date
     */
    public int countHolidaysBetween(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }

        int holidayCount = 0;
        LocalDate current = start;

        while (!current.isAfter(end)) {
            MonthDay monthDay = MonthDay.from(current);
            if (FIXED_HOLIDAYS.contains(monthDay)) {
                holidayCount++;
            }
            current = current.plusDays(1);
        }

        return holidayCount;
    }

    /**
     * Checks if a given date is an official non-working holiday.
     * 
     * @param date date to check
     * @return true if the date is a holiday, false otherwise
     */
    public boolean isHoliday(LocalDate date) {
        MonthDay monthDay = MonthDay.from(date);
        return FIXED_HOLIDAYS.contains(monthDay);
    }
}
