package com.emramirez.islandtrip.web;

import com.emramirez.islandtrip.dto.UpdateRequestDto;
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
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
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
        reservation.setVersion(0L);
        when(reservationService.book(reservation)).thenReturn(reservation);

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
    public void updateReservation_updateRequestGiven_reserveServiceInvocationAnd200ResultExpected() {
        // arrange
        UpdateRequestDto updateRequest = new UpdateRequestDto();
        Reservation reservation = new Reservation();
        reservation.setVersion(0L);
        UUID uuid = UUID.randomUUID();
        WebRequest webRequest = mock(WebRequest.class);
        when(webRequest.getHeader("If-Match")).thenReturn("0");
        when(reservationService.findById(uuid)).thenReturn(reservation);
        when(reservationService.update(updateRequest, uuid)).thenReturn(reservation);

        // act
        ResponseEntity result = reservationController.updateReservation(webRequest, uuid, updateRequest);

        // assert
        verify(reservationService).update(updateRequest, uuid);
        assertThat(result.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void updateReservation_dataIntegrityExceptionThrown_409ResponseExpected() {
        // arrange
        UpdateRequestDto updateRequest = new UpdateRequestDto();
        Reservation reservation = new Reservation();
        reservation.setVersion(0L);
        UUID uuid = UUID.randomUUID();
        WebRequest webRequest = mock(WebRequest.class);
        when(webRequest.getHeader("If-Match")).thenReturn("0");
        when(reservationService.findById(uuid)).thenReturn(reservation);
        when(reservationService.update(updateRequest, uuid)).thenThrow(DataIntegrityViolationException.class);

        // act
        try {
            reservationController.updateReservation(webRequest, uuid, updateRequest);
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
        UpdateRequestDto updateRequest = new UpdateRequestDto();
        Reservation reservation = new Reservation();
        reservation.setVersion(0L);
        UUID uuid = UUID.randomUUID();
        WebRequest webRequest = mock(WebRequest.class);
        when(webRequest.getHeader("If-Match")).thenReturn("0");
        when(reservationService.findById(uuid)).thenReturn(reservation);
        when(reservationService.update(updateRequest, uuid)).thenThrow(ObjectOptimisticLockingFailureException.class);

        // act
        try {
            reservationController.updateReservation(webRequest, uuid, updateRequest);
        } catch (ResponseStatusException ex) {
            // assert
            assertThat(ex.getStatus(), equalTo(HttpStatus.CONFLICT));
            assertThat(ex.getReason(), equalTo("The resource has been updated or deleted by another transaction"));
        } catch (Exception ex) {
            fail("Was expecting a ResponseStatusException but none was thrown");
        }
    }

    @Test
    public void updateReservation_reservationNotFoundGiven_404ResultExpected() {
        // arrange
        UpdateRequestDto updateRequest = new UpdateRequestDto();
        UUID uuid = UUID.randomUUID();

        // act
        ResponseEntity result = reservationController.updateReservation(null, uuid, updateRequest);

        // assert
        assertThat(result.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }
}
