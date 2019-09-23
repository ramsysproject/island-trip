package com.emramirez.islandtrip.service;

import com.emramirez.islandtrip.dto.UpdateRequestDto;
import com.emramirez.islandtrip.model.Reservation;
import com.emramirez.islandtrip.model.ReservationStatus;
import com.emramirez.islandtrip.repository.ReservationRepository;
import com.emramirez.islandtrip.service.status.ReservationStrategy;
import com.emramirez.islandtrip.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * This class handles business interactions with ${@link Reservation} types.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

    private final Validator<Reservation> validator;
    private final ReservationRepository repository;
    private final ReservationStrategy reservationStrategy;

    /**
     * This method persists a new ${@link Reservation} type.
     * @param reservation
     * @return the persisted reservation id
     */
    @Transactional
    public Reservation book(Reservation reservation) {
        validator.validate(reservation);
        reservationStrategy.getHandler(ReservationStatus.ACTIVE).ifPresent(handler -> handler.accept(reservation));

        return repository.save(reservation);
    }

    @Transactional
    public Reservation update(UpdateRequestDto updateRequestDto, UUID id) {
        Reservation currentReservation = findById(id);

        if (( !updateRequestDto.getArrivalDate().equals(currentReservation.getArrivalDate())
                || !updateRequestDto.getDepartureDate().equals(currentReservation.getDepartureDate()) ) &&
                updateRequestDto.getStatus() == ReservationStatus.ACTIVE) {

            log.info("Request to modify reservation dates received");
            updateFields(currentReservation, updateRequestDto);
            reservationStrategy.getHandler(updateRequestDto.getStatus()).ifPresent(it -> it.accept(currentReservation));
        } else {
            updateFields(currentReservation, updateRequestDto);
        }

        return repository.save(currentReservation);
    }

    public Reservation findById(UUID uuid) {
        return  repository.findById(uuid);
    }

    private Reservation updateFields(Reservation reservation, UpdateRequestDto updateRequestDto) {
        reservation.setStatus(updateRequestDto.getStatus());
        reservation.setCustomerName(updateRequestDto.getCustomerName());
        reservation.setCustomerEmail(updateRequestDto.getCustomerEmail());
        reservation.setArrivalDate(updateRequestDto.getArrivalDate());
        reservation.setDepartureDate(updateRequestDto.getDepartureDate());

        return reservation;
    }
}
