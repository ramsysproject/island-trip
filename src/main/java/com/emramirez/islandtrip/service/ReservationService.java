package com.emramirez.islandtrip.service;

import com.emramirez.islandtrip.dto.UpdateRequestDto;
import com.emramirez.islandtrip.exception.ValidationException;
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
    public Reservation book(Reservation reservation) throws ValidationException {
        validator.validate(reservation);
        applyStrategy(reservation, ReservationStatus.ACTIVE);

        return repository.save(reservation);
    }

    /**
     * This methods updates a ${@link Reservation} entity. It applies a strategy based on the reservation status.
     *
     * @param updateRequestDto the dto which holds modified data
     * @param id the entity id
     * @return the updated reservation
     */
    @Transactional
    public Reservation update(UpdateRequestDto updateRequestDto, UUID id) throws ValidationException {
        Reservation currentReservation = findById(id);
        ReservationStatus updatedStatus = updateRequestDto.getStatus();

        if (( !updateRequestDto.getArrivalDate().equals(currentReservation.getArrivalDate())
                || !updateRequestDto.getDepartureDate().equals(currentReservation.getDepartureDate()) ) &&
                updatedStatus == ReservationStatus.ACTIVE) {
            log.info("Request to modify reservation dates received");
            updateFields(currentReservation, updateRequestDto);
            applyStrategy(currentReservation, updatedStatus);
        } else if (updateRequestDto.getStatus() == ReservationStatus.CANCELLED){
            applyStrategy(currentReservation, updatedStatus);
        }
        updateFields(currentReservation, updateRequestDto);
        validator.validate(currentReservation);

        return repository.save(currentReservation);
    }

    private void applyStrategy(Reservation currentReservation, ReservationStatus status) {
        reservationStrategy.getHandler(status).ifPresent(it -> it.accept(currentReservation));
    }

    /**
     * Finds a ${@link Reservation} based on a given id.
     *
     * @param uuid the given id
     * @return the reservation or null otherwise
     */
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
