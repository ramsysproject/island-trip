package com.emramirez.islandtrip.common;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DateUtilsTest {

    @Test
    public void getDaysBetween_threeDaysRangeGiven_threeDaysResultExpected() {
        // arrange
        LocalDate startingDate = LocalDate.of(2019, 01, 01);
        LocalDate endingDate = LocalDate.of(2019, 01, 04);

        // act
        long result = DateUtils.getDaysBetween(startingDate, endingDate);

        // assert
        assertThat(result, equalTo(3L));
    }

    @Test
    public void getDaysBetween_januaryRangeGiven_31DaysResultExpected() {
        // arrange
        LocalDate startingDate = LocalDate.of(2019, 01, 01);
        LocalDate endingDate = LocalDate.of(2019, 02, 01);

        // act
        long result = DateUtils.getDaysBetween(startingDate, endingDate);

        // assert
        assertThat(result, equalTo(31L));
    }

    @Test
    public void getDaysBetween_endingDateGreaterThanStartingDateGiven_zeroDaysResultExpected() {
        // arrange
        LocalDate startingDate = LocalDate.of(2019, 01, 04);
        LocalDate endingDate = LocalDate.of(2019, 01, 02);

        // act
        long result = DateUtils.getDaysBetween(startingDate, endingDate);

        // assert
        assertThat(result, equalTo(0L));
    }
}
