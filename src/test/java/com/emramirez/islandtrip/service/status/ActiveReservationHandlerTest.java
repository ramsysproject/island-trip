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
public class ActiveReservationHandlerTest {

    ActiveReservationHandler handler = new ActiveReservationHandler();

    @Test
    public void accept_reservationWithArrivalAndDepartureRangeOfFiveGiven_activeReservationWithFiveCalendarDatesExpected() {
        // arrange
        Reservation reservation = TestUtils.buildReservationWithCalendarDates(5, ReservationStatus.ACTIVE);

        // act
        handler.accept(reservation);

        // assert
        assertThat(reservation.getCalendarDates().size(), equalTo(5));
        assertThat(reservation.getStatus(), equalTo(ReservationStatus.ACTIVE));
    }

    @Test
    public void accept_reservationWithoutCalendarDatesGiven_reservationWithCalendarDatesExpected() {
        // arrange
        Reservation reservation = TestUtils.buildReservationWithCalendarDates(5, ReservationStatus.ACTIVE);
        reservation.setCalendarDates(null);

        // act
        handler.accept(reservation);

        // assert
        assertThat(reservation.getCalendarDates().size(), equalTo(5));
        assertThat(reservation.getStatus(), equalTo(ReservationStatus.ACTIVE));
    }
}
