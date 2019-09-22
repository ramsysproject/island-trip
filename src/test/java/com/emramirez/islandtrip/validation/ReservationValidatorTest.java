package com.emramirez.islandtrip.validation;

import com.emramirez.islandtrip.model.Reservation;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class ReservationValidatorTest {

    @Mock
    Clock clock;

    @InjectMocks
    ReservationValidator reservationValidator;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    private Clock fixedClock;

    @Test
    public void validate_nullDatesGiven_exceptionExpected() {
        // arrange
        LocalDate startingDate = null;
        LocalDate endingDate = null;
        Reservation reservation = buildReservation(startingDate, endingDate);
        expectedEx.expect(NullPointerException.class);
        expectedEx.expectMessage("The reservation arrival date cannot be null");

        // act
        reservationValidator.validate(reservation);
    }

    @Test
    public void validate_nullArrivalDateGiven_exceptionExpected() {
        // arrange
        LocalDate startingDate = null;
        LocalDate endingDate = LocalDate.of(2019, 9, 15);
        Reservation reservation = buildReservation(startingDate, endingDate);
        expectedEx.expect(NullPointerException.class);
        expectedEx.expectMessage("The reservation arrival date cannot be null");

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
        expectedEx.expectMessage("The reservation departure date cannot be null");

        // act
        reservationValidator.validate(reservation);
    }

    @Test
    public void validate_arrivalAfterDepartureDateGiven_exceptionExpected() {
        // arrange
        LocalDate startingDate = LocalDate.of(2019, 9, 15);
        LocalDate endingDate = LocalDate.of(2019, 9, 10);
        Reservation reservation = buildReservation(startingDate, endingDate);
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("The reservation arrival date cannot be after departure date");

        // act
        reservationValidator.validate(reservation);
    }

    @Test
    public void validate_reservationRangeGreaterThanThreeDaysGiven_exceptionExpected() {
        // arrange
        LocalDate startingDate = LocalDate.of(2019, 9, 15);
        LocalDate endingDate = LocalDate.of(2019, 9, 19);
        Reservation reservation = buildReservation(startingDate, endingDate);
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("The maximum reservation period is 3 days");

        // act
        reservationValidator.validate(reservation);
    }

    @Test
    public void validate_reservationForCurrentDayGiven_exceptionExpected() {
        // arrange
        LocalDate startingDate = LocalDate.now();
        LocalDate endingDate = startingDate.plusDays(1);
        Reservation reservation = buildReservation(startingDate, endingDate);
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("The reservation must be at least 1 day ahead");
        fixedClock = Clock.fixed(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.of("UTC"));
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();

        // act
        reservationValidator.validate(reservation);
    }

    @Test
    public void validate_reservationWithValidRangeGiven_noExceptionExpected() {
        // arrange
        LocalDate startingDate = LocalDate.now().plusDays(1);
        LocalDate endingDate = startingDate.plusDays(3);
        Reservation reservation = buildReservation(startingDate, endingDate);
        fixedClock = Clock.fixed(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.of("UTC"));
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();

        // act
        reservationValidator.validate(reservation);
    }

    private Reservation buildReservation(LocalDate startingDate, LocalDate endingDate) {
        Reservation reservation = new Reservation();
        reservation.setArrivalDate(startingDate);
        reservation.setDepartureDate(endingDate);

        return reservation;
    }
}
