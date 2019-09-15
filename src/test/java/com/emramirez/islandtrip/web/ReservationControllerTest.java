package com.emramirez.islandtrip.web;

import com.emramirez.islandtrip.model.Reservation;
import com.emramirez.islandtrip.service.ReservationService;
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
public class ReservationControllerTest {

    @Mock
    ReservationService reservationService;
    @InjectMocks
    ReservationController reservationController;

    @Test
    public void createReservation_reservationGiven_reserveServiceInvocationAnd201ResultExpected() {
        // arrange
        Reservation reservation = new Reservation();

        // act
        ResponseEntity result = reservationController.createReservation(reservation);

        // assert
        verify(reservationService).reserve(reservation);
        assertThat(result.getStatusCode(), equalTo(HttpStatus.CREATED));
    }
}
