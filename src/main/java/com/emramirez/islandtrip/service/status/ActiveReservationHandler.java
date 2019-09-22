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
        Set<CalendarDate> calendarDateSet = associateCalendarDates(reservation);
        reservation.setCalendarDates(calendarDateSet);
        reservation.setStatus(this.supports());
    }

    private Set<CalendarDate> associateCalendarDates(Reservation reservation) {
        long bookedDays = DateUtils.getDaysBetween(reservation.getArrivalDate(), reservation.getDepartureDate());
        return IntStream.range(0, (int) bookedDays)
                .mapToObj(value -> {
                    CalendarDate calendarDate = new CalendarDate();
                    calendarDate.setDate(reservation.getArrivalDate().plusDays(value));
                    calendarDate.setStatus(CalendarDateStatus.BOOKED);
                    return calendarDate;
                }).collect(Collectors.toSet());
    }

    @Override
    public ReservationStatus supports() {
        return ReservationStatus.ACTIVE;
    }
}
