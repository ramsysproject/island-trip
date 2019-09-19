package com.emramirez.islandtrip.service;

import com.emramirez.islandtrip.model.CalendarDate;
import com.emramirez.islandtrip.repository.CalendarDateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        List<CalendarDate> calendarDateList =
                calendarDateRepository.findByCalendarDateBetween(LocalDate.now(), LocalDate.now().plusDays(range));

        List<CalendarDate> freeDates = IntStream.range(0, range)
                .skip(1)
                .mapToObj(value -> {
                    CalendarDate calendarDate = new CalendarDate();
                    calendarDate.setCalendarDate(LocalDate.now().plusDays(value));
                    return calendarDate;
                })
                .filter(calendarDate -> !calendarDateList.contains(calendarDate))
                .collect(Collectors.toList());

        return freeDates;
    }
}
