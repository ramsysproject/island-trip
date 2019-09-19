package com.emramirez.islandtrip.service;

import com.emramirez.islandtrip.common.DateUtils;
import com.emramirez.islandtrip.model.CalendarDate;
import com.emramirez.islandtrip.model.CalendarDateStatus;
import com.emramirez.islandtrip.model.Reservation;
import com.emramirez.islandtrip.repository.ReservationRepository;
import com.emramirez.islandtrip.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class handles business interactions with ${@link Reservation} types.
 */
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final Validator<Reservation> validator;
    private final ReservationRepository repository;

    /**
     * This method persists a new ${@link Reservation} type.
     * @param reservation
     * @return the persisted reservation id
     */
    @Transactional
    public Reservation book(Reservation reservation) {
        validator.validate(reservation);
        Set<CalendarDate> calendarDateSet = associateCalendarDates(reservation);
        reservation.setCalendarDates(calendarDateSet);

        return repository.save(reservation);
    }

    private Set<CalendarDate> associateCalendarDates(Reservation reservation) {
        long bookedDays = DateUtils.getDaysBetween(reservation.getStartingDate(), reservation.getEndingDate());
        return IntStream.range(0, (int) bookedDays)
                .mapToObj(value -> {
                    CalendarDate calendarDate = new CalendarDate();
                    calendarDate.setCalendarDate(reservation.getStartingDate().plusDays(value));
                    calendarDate.setStatus(CalendarDateStatus.BOOKED);
                    return calendarDate;
                }).collect(Collectors.toSet());
    }
}
