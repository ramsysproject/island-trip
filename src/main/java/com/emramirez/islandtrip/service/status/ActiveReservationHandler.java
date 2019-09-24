package com.emramirez.islandtrip.service.status;

import com.emramirez.islandtrip.common.DateUtils;
import com.emramirez.islandtrip.model.CalendarDate;
import com.emramirez.islandtrip.model.CalendarDateStatus;
import com.emramirez.islandtrip.model.Reservation;
import com.emramirez.islandtrip.model.ReservationStatus;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ActiveReservationHandler implements ReservationStatusHandler {

    @Override
    public void accept(Reservation reservation) {
        Set<CalendarDate> calendarDatesRange = getCalendarDatesRange(reservation);
        if (reservation.getCalendarDates() != null) {
            reservation.getCalendarDates().addAll(calendarDatesRange);
            reservation.getCalendarDates().removeAll(getOrphans(reservation, calendarDatesRange));
        } else {
            reservation.setCalendarDates(calendarDatesRange);
        }

        reservation.setStatus(this.supports());
    }

    private Set<CalendarDate> getOrphans(Reservation reservation, Set<CalendarDate> calendarDatesRange) {
        return reservation.getCalendarDates()
                .stream()
                .filter(it -> !calendarDatesRange.contains(it))
                .collect(Collectors.toSet());
    }

    private Set<CalendarDate> getCalendarDatesRange(Reservation reservation) {
        long bookedDays = DateUtils.getDaysBetween(reservation.getArrivalDate(), reservation.getDepartureDate());
        return IntStream.range(0, (int) bookedDays)
                .mapToObj(value -> buildCalendarDate(reservation, value))
                .collect(Collectors.toSet());
    }

    private CalendarDate buildCalendarDate(Reservation reservation, int value) {
        CalendarDate calendarDate = new CalendarDate();
        calendarDate.setDate(reservation.getArrivalDate().plusDays(value));
        calendarDate.setStatus(CalendarDateStatus.BOOKED);
        return calendarDate;
    }

    @Override
    public ReservationStatus supports() {
        return ReservationStatus.ACTIVE;
    }
}
