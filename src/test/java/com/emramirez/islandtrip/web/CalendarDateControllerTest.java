package com.emramirez.islandtrip.web;

import com.emramirez.islandtrip.service.CalendarDateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CalendarDateControllerTest {

    @Mock
    CalendarDateService calendarDateService;

    @InjectMocks
    CalendarDateController calendarDateController;

    @Test
    public void getAvailableDates_rangeGiven_calendarDateServiceInvocationAnd200ResultExpected() {
        // arrange
        int range = 15;

        // act
        ResponseEntity result = calendarDateController.getAvailableDates(range);

        // assert
        verify(calendarDateService).getAvailableCalendarDates(range);
        assertThat(result.getStatusCode(), equalTo(HttpStatus.OK));
    }
}
