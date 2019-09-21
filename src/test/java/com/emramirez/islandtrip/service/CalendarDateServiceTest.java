package com.emramirez.islandtrip.service;

import com.emramirez.islandtrip.model.CalendarDate;
import com.emramirez.islandtrip.model.CalendarDateStatus;
import com.emramirez.islandtrip.repository.CalendarDateRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CalendarDateServiceTest {

    @Mock
    CalendarDateRepository repository;

    @InjectMocks
    CalendarDateService calendarDateService;

    @Test
    public void getAvailableCalendarDates_tenDaysRangeAndAllAreFreeGiven_allButFirstDayListExpected() {
        // arrange
        int range = 10;
        when(repository.
                findByDateBetweenAndStatus(LocalDate.now(), LocalDate.now().plusDays(range), CalendarDateStatus.BOOKED)
            ).thenReturn(new ArrayList<>());

        // act
        List<CalendarDate> availableDates = calendarDateService.getAvailableCalendarDates(range);

        // assert
        assertThat(availableDates.size(), equalTo(9));
    }

    @Test
    public void getAvailableCalendarDates_tenDaysRangeAndAllAreBookedGiven_emptyAvailableDatesExpected() {
        // arrange
        int range = 10;
        when(repository.
                findByDateBetweenAndStatus(LocalDate.now(), LocalDate.now().plusDays(range), CalendarDateStatus.BOOKED)
        ).thenReturn(getCalendarDatesCollection(10));

        // act
        List<CalendarDate> availableDates = calendarDateService.getAvailableCalendarDates(range);

        // assert
        assertThat(availableDates.isEmpty(), equalTo(true));
    }

    @Test
    public void getAvailableCalendarDates_tenDaysRangeAndHalfAreBookedGiven_fiveAvailableDaysExpected() {
        // arrange
        int range = 10;
        when(repository.
                findByDateBetweenAndStatus(LocalDate.now(), LocalDate.now().plusDays(range), CalendarDateStatus.BOOKED)
        ).thenReturn(getCalendarDatesCollection(5));

        // act
        List<CalendarDate> availableDates = calendarDateService.getAvailableCalendarDates(range);

        // assert
        assertThat(availableDates.size(), equalTo(5));
    }

    private List<CalendarDate> getCalendarDatesCollection(int range) {
        return IntStream.range(0, range)
                .mapToObj(value -> {
                    CalendarDate calendarDate = new CalendarDate();
                    calendarDate.setDate(LocalDate.now().plusDays(value));
                    return calendarDate;
                })
                .collect(toList());
    }
}
