package com.emramirez.islandtrip.service;

import com.emramirez.islandtrip.model.Reservation;
import com.emramirez.islandtrip.model.ReservationStatus;
import com.emramirez.islandtrip.repository.ReservationRepository;
import com.emramirez.islandtrip.service.status.ReservationStrategy;
import com.emramirez.islandtrip.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * This class handles business interactions with ${@link Reservation} types.
 */
@Service
@RequiredArgsConstructor
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
    public Reservation update(Reservation reservation, UUID id) {
        reservation.setId(id);
        validator.validate(reservation);
        reservationStrategy.getHandler(reservation.getStatus()).ifPresent(handler -> handler.accept(reservation));

        return repository.save(reservation);
    }
}
