package com.emramirez.islandtrip.validation;

import com.emramirez.islandtrip.model.Reservation;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

@RunWith(MockitoJUnitRunner.class)
public class ReservationValidatorTest {

    ReservationValidator reservationValidator;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp() {
        reservationValidator = new ReservationValidator();
    }

    @Test
    public void validate_nullDatesGiven_exceptionExpected() {
        // arrange
        LocalDate startingDate = null;
        LocalDate endingDate = null;
        Reservation reservation = buildReservation(startingDate, endingDate);
        expectedEx.expect(NullPointerException.class);
        expectedEx.expectMessage("The reservation starting date cannot be null");

        // act
        reservationValidator.validate(reservation);
    }

    @Test
    public void validate_nullStartingDateGiven_exceptionExpected() {
        // arrange
        LocalDate startingDate = null;
        LocalDate endingDate = LocalDate.of(2019, 9, 15);
        Reservation reservation = buildReservation(startingDate, endingDate);
        expectedEx.expect(NullPointerException.class);
        expectedEx.expectMessage("The reservation starting date cannot be null");

        // act
        reservationValidator.validate(reservation);
    }

    @Test
    public void validate_nullEndingDateGiven_exceptionExpected() {
        // arrange
        LocalDate startingDate = LocalDate.of(2019, 9, 15);
        LocalDate endingDate = null;
        Reservation reservation = buildReservation(startingDate, endingDate);
        expectedEx.expect(NullPointerException.class);
        expectedEx.expectMessage("The reservation ending date cannot be null");

        // act
        reservationValidator.validate(reservation);
    }

    private Reservation buildReservation(LocalDate startingDate, LocalDate endingDate) {
        Reservation reservation = new Reservation();
        reservation.setStartingDate(startingDate);
        reservation.setEndingDate(endingDate);

        return reservation;
    }
}
