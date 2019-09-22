package com.emramirez.islandtrip.repository;

import com.emramirez.islandtrip.model.Reservation;
import com.emramirez.islandtrip.model.ReservationStatus;
import com.emramirez.islandtrip.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ReservationRepositoryIT {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReservationRepository repository;

    @Test
    public void save_noCalendarDatesGiven_plainReservationExpected() {
        // arrange
        Reservation reservation = TestUtils.buildReservationWithCalendarDates(0, ReservationStatus.ACTIVE);

        // act
        Reservation result = repository.save(reservation);

        // assert
        assertThat(result.getCalendarDates().isEmpty(), equalTo(true));
    }

    @Test
    public void save_fiveDaysCalendarDatesRangeGiven_reservationAndRelatedDatesExpected() {
        // arrange
        Reservation reservation = TestUtils.buildReservationWithCalendarDates(5, ReservationStatus.ACTIVE);

        // act
        Reservation result = repository.save(reservation);

        // assert
        assertThat(result.getCalendarDates().size(), equalTo(5));
    }
}
