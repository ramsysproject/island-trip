package com.emramirez.islandtrip.utils;

import com.emramirez.islandtrip.model.CalendarDate;

import java.time.LocalDate;

public class TestUtils {

    public static CalendarDate buildTodayCalendarDate() {
        CalendarDate calendarDate = new CalendarDate();
        calendarDate.setDate(LocalDate.now());

        return calendarDate;
    }
}
