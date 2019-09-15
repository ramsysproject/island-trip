package com.emramirez.islandtrip.service;

import com.emramirez.islandtrip.model.Reservation;
import com.emramirez.islandtrip.repository.ReservationRepository;
import com.emramirez.islandtrip.validation.ReservationValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReservationServiceTest {

    public static final Long RESERVATION_ID = 1L;
    @Mock
    ReservationValidator validator;
    @Mock
    ReservationRepository repository;
    @InjectMocks
    ReservationService reservationService;

    @Test
    public void reserve_validInputGiven_reservationIdExpected() {
        // arrange
        Reservation reservation = new Reservation();
        Reservation savedReservation = buildResult();
        when(repository.save(reservation)).thenReturn(savedReservation);

        // act
        Long reservationId = reservationService.reserve(reservation);

        // assert
        assertThat(reservationId, equalTo(RESERVATION_ID));
        verify(repository).save(reservation);
        verify(validator).validate(reservation);
    }

    private Reservation buildResult() {
        Reservation reservation = new Reservation();
        reservation.setId(RESERVATION_ID);
        return reservation;
    }
}
