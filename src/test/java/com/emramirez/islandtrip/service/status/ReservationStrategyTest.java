package com.emramirez.islandtrip.service.status;

import com.emramirez.islandtrip.model.ReservationStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ReservationStrategyTest {

    ReservationStrategy reservationStrategy;

    @Test
    public void getHandler_activeStatusHandlerGiven_optionalWithHandlerExpected() {
        // arrange
        List<ReservationStatusHandler> reservationStatusHandlers =
                Arrays.asList(new ActiveReservationHandler());
        reservationStrategy = new ReservationStrategy(reservationStatusHandlers);

        // act
        Optional<ReservationStatusHandler> handler = reservationStrategy.getHandler(ReservationStatus.ACTIVE);

        // assert
        assertThat(handler.isPresent(), equalTo(true));
        assertThat(handler.get().getClass(), equalTo(ActiveReservationHandler.class));
    }

    @Test
    public void getHandler_multipleHandlersInitializedAndActiveAskedForGiven_activeHandlerExpected() {
        // arrange
        List<ReservationStatusHandler> reservationStatusHandlers =
                Arrays.asList(new ActiveReservationHandler(), new CancelledReservationHandler());
        reservationStrategy = new ReservationStrategy(reservationStatusHandlers);

        // act
        Optional<ReservationStatusHandler> handler = reservationStrategy.getHandler(ReservationStatus.ACTIVE);

        // assert
        assertThat(handler.isPresent(), equalTo(true));
        assertThat(handler.get().getClass(), equalTo(ActiveReservationHandler.class));
    }

    @Test
    public void getHandler_noHandlersGiven_emptyOptionalExpected() {
        // arrange
        reservationStrategy = new ReservationStrategy(new ArrayList<>());

        // act
        Optional<ReservationStatusHandler> handler = reservationStrategy.getHandler(ReservationStatus.ACTIVE);

        // assert
        assertThat(handler.isPresent(), equalTo(false));
    }
}
