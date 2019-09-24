package com.emramirez.islandtrip.service;

import com.emramirez.islandtrip.dto.UpdateRequestDto;
import com.emramirez.islandtrip.model.Reservation;
import com.emramirez.islandtrip.model.ReservationStatus;
import com.emramirez.islandtrip.repository.ReservationRepository;
import com.emramirez.islandtrip.service.status.CancelledReservationHandler;
import com.emramirez.islandtrip.service.status.ReservationStrategy;
import com.emramirez.islandtrip.utils.TestUtils;
import com.emramirez.islandtrip.validation.ReservationValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReservationServiceTest {

    public static final UUID RESERVATION_ID = UUID.randomUUID();

    @Mock
    ReservationValidator validator;

    @Mock
    ReservationRepository repository;

    @Mock
    ReservationStrategy reservationStrategy;

    @Mock
    CancelledReservationHandler cancelledReservationHandler;

    @InjectMocks
    ReservationService reservationService;

    @Test
    public void book_validInputGiven_reservationIdExpected() {
        // arrange
        Reservation reservation = buildReservation();
        Reservation savedReservation = buildResult();
        when(repository.save(reservation)).thenReturn(savedReservation);

        // act
        Reservation reservationResult = reservationService.book(reservation);

        // assert
        assertThat(reservationResult.getId(), equalTo(RESERVATION_ID));
        verify(repository).save(reservation);
        verify(validator).validate(reservation);
        verify(reservationStrategy).getHandler(ReservationStatus.ACTIVE);
    }

    @Test
    public void update_unmodifiedReservationDatesGiven_saveAndNoStrategyInvokedExpected() {
        // arrange
        UpdateRequestDto updateRequestDto = buildUpdateRequest(ReservationStatus.ACTIVE);
        Reservation savedReservation = buildResult();
        when(repository.findById(any(UUID.class))).thenReturn(buildReservation());
        when(repository.save(any())).thenReturn(savedReservation);

        // act
        reservationService.update(updateRequestDto, RESERVATION_ID);

        // assert
        verify(repository).save(any());
        verifyZeroInteractions(reservationStrategy);
    }

    @Test
    public void update_modifiedArrivalAndDepartureDateGiven_saveAndStrategyInvokedExpected() {
        // arrange
        UpdateRequestDto updateRequestDto = buildUpdateRequest(ReservationStatus.ACTIVE);
        updateRequestDto.setArrivalDate(LocalDate.now().plusDays(2));
        updateRequestDto.setDepartureDate(LocalDate.now().plusDays(3));
        Reservation savedReservation = buildResult();
        when(repository.findById(any(UUID.class))).thenReturn(buildReservation());
        when(repository.save(any())).thenReturn(savedReservation);

        // act
        reservationService.update(updateRequestDto, RESERVATION_ID);

        // assert
        verify(repository).save(any());
        verify(reservationStrategy).getHandler(updateRequestDto.getStatus());
    }

    @Test
    public void update_modifiedArrivalDateGiven_saveAndStrategyInvokedExpected() {
        // arrange
        UpdateRequestDto updateRequestDto = buildUpdateRequest(ReservationStatus.ACTIVE);
        updateRequestDto.setArrivalDate(LocalDate.now().plusDays(2));
        Reservation savedReservation = buildResult();
        when(repository.findById(any(UUID.class))).thenReturn(buildReservation());
        when(repository.save(any())).thenReturn(savedReservation);

        // act
        reservationService.update(updateRequestDto, RESERVATION_ID);

        // assert
        verify(repository).save(any());
        verify(reservationStrategy).getHandler(updateRequestDto.getStatus());
    }

    @Test
    public void update_modifiedDepartureDateGiven_saveAndStrategyInvokedExpected() {
        // arrange
        UpdateRequestDto updateRequestDto = buildUpdateRequest(ReservationStatus.ACTIVE);
        updateRequestDto.setDepartureDate(LocalDate.now().plusDays(2));
        Reservation savedReservation = buildResult();
        when(repository.findById(any(UUID.class))).thenReturn(buildReservation());
        when(repository.save(any())).thenReturn(savedReservation);

        // act
        reservationService.update(updateRequestDto, RESERVATION_ID);

        // assert
        verify(repository).save(any());
        verify(reservationStrategy).getHandler(updateRequestDto.getStatus());
    }

    @Test
    public void update_cancelledStatusGiven_saveAndStrategyInvokedExpected() {
        // arrange
        UpdateRequestDto updateRequestDto = buildUpdateRequest(ReservationStatus.CANCELLED);
        Reservation savedReservation = buildResult();
        when(repository.findById(any(UUID.class))).thenReturn(buildReservation());
        when(repository.save(any())).thenReturn(savedReservation);

        // act
        reservationService.update(updateRequestDto, RESERVATION_ID);

        // assert
        verify(repository).save(any());
        verify(reservationStrategy).getHandler(updateRequestDto.getStatus());
    }

    @Test
    public void update_validStatusGiven_strategyFoundAndInvokedExpected() {
        // arrange
        UpdateRequestDto updateRequestDto = buildUpdateRequest(ReservationStatus.CANCELLED);
        Reservation savedReservation = buildResult();
        when(repository.findById(any(UUID.class))).thenReturn(buildReservation());
        when(repository.save(any())).thenReturn(savedReservation);
        when(reservationStrategy.getHandler(updateRequestDto.getStatus()))
                .thenReturn(Optional.of(cancelledReservationHandler));

        // act
        reservationService.update(updateRequestDto, RESERVATION_ID);

        // assert
        verify(repository).save(any());
        verify(reservationStrategy).getHandler(updateRequestDto.getStatus());
        verify(cancelledReservationHandler).accept(any());
    }

    private Reservation buildReservation() {
        Reservation reservation = new Reservation();
        reservation.setArrivalDate(LocalDate.now());
        reservation.setDepartureDate(LocalDate.now().plusDays(1));
        return reservation;
    }

    private UpdateRequestDto buildUpdateRequest(ReservationStatus status) {
        UpdateRequestDto updateRequest = new UpdateRequestDto();
        updateRequest.setArrivalDate(LocalDate.now());
        updateRequest.setDepartureDate(LocalDate.now().plusDays(1));
        updateRequest.setStatus(status);
        return updateRequest;
    }

    private Reservation buildResult() {
        Reservation reservation = new Reservation();
        reservation.setId(RESERVATION_ID);
        reservation.setArrivalDate(LocalDate.now());
        reservation.setDepartureDate(LocalDate.now().plusDays(1));
        reservation.setCalendarDates(Collections.singleton(TestUtils.buildTodayCalendarDate()));
        return reservation;
    }
}
