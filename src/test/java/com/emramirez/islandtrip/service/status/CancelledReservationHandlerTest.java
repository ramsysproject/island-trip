package com.emramirez.islandtrip.service.status;

import com.emramirez.islandtrip.model.Reservation;
import com.emramirez.islandtrip.model.ReservationStatus;
import com.emramirez.islandtrip.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CancelledReservationHandlerTest {

    CancelledReservationHandler handler = new CancelledReservationHandler();

    @Test
    public void accept_reservationActiveWithDatesGiven_reservationCancelledAndWithoutDatesExpected() {
        // arrange
        Reservation reservation = TestUtils.buildReservationWithCalendarDates(10, ReservationStatus.ACTIVE);

        // act
        handler.accept(reservation);

        // assert
        assertThat(reservation.getCalendarDates().isEmpty(), equalTo(true));
        assertThat(reservation.getStatus(), equalTo(ReservationStatus.CANCELLED));
    }
}
