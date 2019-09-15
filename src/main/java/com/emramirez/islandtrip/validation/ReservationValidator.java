package com.emramirez.islandtrip.validation;

import com.emramirez.islandtrip.model.Reservation;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ReservationValidator implements Validator<Reservation> {

    @Override
    public void validate(Reservation reservation) {
        Objects.requireNonNull(reservation.getStartingDate(), "The reservation starting date cannot be null");
        Objects.requireNonNull(reservation.getEndingDate(), "The reservation ending date cannot be null");
    }
}
