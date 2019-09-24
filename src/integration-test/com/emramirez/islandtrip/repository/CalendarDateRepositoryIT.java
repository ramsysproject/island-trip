package com.emramirez.islandtrip.repository;

import com.emramirez.islandtrip.model.CalendarDate;
import com.emramirez.islandtrip.model.CalendarDateStatus;
import com.emramirez.islandtrip.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CalendarDateRepositoryIT {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CalendarDateRepository repository;

    @Test
    public void findByDateBetweenAndStatus_validInputGiven_calendarDatesExpected() {
        // arrange
        Set<CalendarDate> calendarDates = TestUtils.buildCalendarDatesForRange(5);
        calendarDates.forEach(entityManager::persist);
        Map<LocalDate, CalendarDate> calendarDateMap = calendarDates.stream()
                .collect(toMap(k -> k.getDate(), v -> v));

        // act
        List<CalendarDate> result = repository
                .findByDateBetweenAndStatus(LocalDate.now(), LocalDate.now().plusDays(5), CalendarDateStatus.BOOKED);

        // assert
        assertThat(result.size(), equalTo(5));
        result.stream().forEach(calendarDate -> {
            assertThat(calendarDateMap.containsKey(calendarDate.getDate()), equalTo(true));
            assertThat(calendarDate.getStatus(), equalTo(CalendarDateStatus.BOOKED));
        });
    }
}
