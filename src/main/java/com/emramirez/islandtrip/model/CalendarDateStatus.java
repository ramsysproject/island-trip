package com.emramirez.islandtrip.model;

/**
 * Contains the possible statuses of ${@link CalendarDate}. At the moment we are only interested in booked dates,
 * but in the future we may need to hold extra ones, like not-operational ones.
 */
public enum CalendarDateStatus {
    AVAILABLE,
    BOOKED;
}
