package com.emramirez.islandtrip.service;

import com.emramirez.islandtrip.model.Reservation;
import com.emramirez.islandtrip.repository.ReservationRepository;
import com.emramirez.islandtrip.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * This class handles business interactions with ${@link Reservation} types.
 */
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final Validator<Reservation> validator;
    private final ReservationRepository repository;

    /**
     * This method persists a new ${@link Reservation} type.
     * @param reservation
     * @return the persisted reservation id
     */
    public Reservation book(Reservation reservation) {
        validator.validate(reservation);
        Reservation result = repository.save(reservation);

        return result;
    }
}
