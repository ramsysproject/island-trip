package com.emramirez.islandtrip.utils;

import com.emramirez.islandtrip.model.CalendarDate;
import com.emramirez.islandtrip.model.CalendarDateStatus;
import com.emramirez.islandtrip.model.Reservation;
import com.emramirez.islandtrip.model.ReservationStatus;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestUtils {

    public static Reservation buildReservationWithCalendarDates(int range, ReservationStatus status) {
        Reservation reservation = new Reservation();
        reservation.setStatus(status);
        reservation.setArrivalDate(LocalDate.now().plusDays(1));
        reservation.setDepartureDate(LocalDate.now().plusDays(1).plusDays(range));
        reservation.setCalendarDates(buildCalendarDatesForRange(range));

        return reservation;
    }

    private static Set<CalendarDate> buildCalendarDatesForRange(int range) {
        return IntStream.range(0, range)
                .mapToObj(value -> {
                    CalendarDate calendarDate = new CalendarDate();
                    calendarDate.setDate(LocalDate.now().plusDays(value));
                    calendarDate.setStatus(CalendarDateStatus.BOOKED);
                    return calendarDate;
                }).collect(Collectors.toSet());
    }

    public static CalendarDate buildTodayCalendarDate() {
        CalendarDate calendarDate = new CalendarDate();
        calendarDate.setDate(LocalDate.now());

        return calendarDate;
    }
}
