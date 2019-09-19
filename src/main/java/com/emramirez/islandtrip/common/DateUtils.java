package com.emramirez.islandtrip.common;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public final class DateUtils {

    private DateUtils() {}

    /**
     * This methods gets the difference in days between two given dates.
     * @param startingDate
     * @param endingDate
     * @return the difference in days
     */
    public static long getDaysBetween(LocalDate startingDate, LocalDate endingDate) {
        long daysBetween = DAYS.between(startingDate, endingDate);

        return Math.max(0, daysBetween);
    }
}
