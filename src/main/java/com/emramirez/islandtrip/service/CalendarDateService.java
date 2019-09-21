package com.emramirez.islandtrip.service;

import com.emramirez.islandtrip.model.CalendarDate;
import com.emramirez.islandtrip.model.CalendarDateStatus;
import com.emramirez.islandtrip.repository.CalendarDateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class CalendarDateService {

    private final CalendarDateRepository calendarDateRepository;

    /**
     * Returns the available dates for booking. As a rule, the customers needs to book at least 1 day ahead of arrival.
     *
     * @param range how many days ahead the availability is required
     * @return a list of the available dates
     */
    public List<CalendarDate> getAvailableCalendarDates(int range) {
        List<LocalDate> bookedDates =
                calendarDateRepository.findByDateBetweenAndStatus(
                            LocalDate.now(), LocalDate.now().plusDays(range), CalendarDateStatus.BOOKED)
                        .stream()
                        .map(CalendarDate::getDate)
                        .collect(toList());

        List<CalendarDate> freeCalendarDates = IntStream.range(0, range)
                .skip(1)
                .mapToObj(value -> getCalendarDate(value))
                .filter(calendarDate -> !bookedDates.contains(calendarDate.getDate()))
                .collect(toList());

        return freeCalendarDates;
    }

    private CalendarDate getCalendarDate(int value) {
        CalendarDate calendarDate = new CalendarDate();
        calendarDate.setDate(LocalDate.now().plusDays(value));
        calendarDate.setStatus(CalendarDateStatus.AVAILABLE);

        return calendarDate;
    }
}
