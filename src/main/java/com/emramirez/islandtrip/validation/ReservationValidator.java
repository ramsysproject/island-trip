package com.emramirez.islandtrip.validation;

import com.emramirez.islandtrip.common.DateUtils;
import com.emramirez.islandtrip.model.Reservation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;

@Component
public class ReservationValidator implements Validator<Reservation> {

    @Override
    public void validate(Reservation reservation) {
        LocalDate startingDate = reservation.getStartingDate();
        LocalDate endingDate = reservation.getEndingDate();

        Objects.requireNonNull(startingDate, "The reservation starting date cannot be null");
        Objects.requireNonNull(endingDate, "The reservation ending date cannot be null");

        if (DateUtils.getDaysBetween(startingDate, endingDate) > 3) {
            throw new IllegalArgumentException("The maximum reservation period is 3 days");
        }

        // TODO validate reserve is 1 day ahead and up to 1 month in advance
    }
}
