package com.emramirez.islandtrip.web;

import com.emramirez.islandtrip.model.Reservation;
import com.emramirez.islandtrip.service.ReservationService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReservationControllerTest {

    @Mock
    ReservationService reservationService;

    @InjectMocks
    ReservationController reservationController;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void createReservation_reservationGiven_reserveServiceInvocationAnd201ResultExpected() {
        // arrange
        Reservation reservation = new Reservation();

        // act
        ResponseEntity result = reservationController.createReservation(reservation);

        // assert
        verify(reservationService).book(reservation);
        assertThat(result.getStatusCode(), equalTo(HttpStatus.CREATED));
    }

    @Test
    public void createReservation_dataIntegrityExceptionThrown_409ResponseExpected() {
        // arrange
        Reservation reservation = new Reservation();
        when(reservationService.book(reservation)).thenThrow(DataIntegrityViolationException.class);

        // act
        try {
            reservationController.createReservation(reservation);
        } catch (ResponseStatusException ex) {
            // assert
            assertThat(ex.getStatus(), equalTo(HttpStatus.CONFLICT));
            assertThat(ex.getReason(), equalTo("The provided date range is no longer available for booking"));
        } catch (Exception ex) {
            fail("Was expecting a ResponseStatusException but none was thrown");
        }
    }

    @Test
    public void updateReservation_reservationGiven_reserveServiceInvocationAnd200ResultExpected() {
        // arrange
        Reservation reservation = new Reservation();
        UUID uuid = UUID.randomUUID();

        // act
        ResponseEntity result = reservationController.updateReservation(uuid, reservation);

        // assert
        verify(reservationService).update(reservation, uuid);
        assertThat(result.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void updateReservation_dataIntegrityExceptionThrown_409ResponseExpected() {
        // arrange
        Reservation reservation = new Reservation();
        UUID uuid = UUID.randomUUID();
        when(reservationService.update(reservation, uuid)).thenThrow(DataIntegrityViolationException.class);

        // act
        try {
            reservationController.updateReservation(uuid, reservation);
        } catch (ResponseStatusException ex) {
            // assert
            assertThat(ex.getStatus(), equalTo(HttpStatus.CONFLICT));
            assertThat(ex.getReason(), equalTo("The provided date range is no longer available for booking"));
        } catch (Exception ex) {
            fail("Was expecting a ResponseStatusException but none was thrown");
        }
    }

    @Test
    public void updateReservation_optimisticLockingExceptionThrown_409ResponseExpected() {
        // arrange
        Reservation reservation = new Reservation();
        UUID uuid = UUID.randomUUID();
        when(reservationService.update(reservation, uuid)).thenThrow(ObjectOptimisticLockingFailureException.class);

        // act
        try {
            reservationController.updateReservation(uuid, reservation);
        } catch (ResponseStatusException ex) {
            // assert
            assertThat(ex.getStatus(), equalTo(HttpStatus.CONFLICT));
            assertThat(ex.getReason(), equalTo("The resource has been updated or deleted by another transaction"));
        } catch (Exception ex) {
            fail("Was expecting a ResponseStatusException but none was thrown");
        }
    }
}
