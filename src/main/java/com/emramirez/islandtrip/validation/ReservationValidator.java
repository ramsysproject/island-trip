package com.emramirez.islandtrip.validation;

import com.emramirez.islandtrip.common.DateUtils;
import com.emramirez.islandtrip.model.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ReservationValidator implements Validator<Reservation> {

    private final Clock clock;

    @Override
    public void validate(Reservation reservation) {
        LocalDate startingDate = reservation.getStartingDate();
        LocalDate endingDate = reservation.getEndingDate();

        Objects.requireNonNull(startingDate, "The reservation starting date cannot be null");
        Objects.requireNonNull(endingDate, "The reservation ending date cannot be null");

        if (startingDate.isAfter(endingDate)) {
            throw new IllegalArgumentException("Starting date cannot be after ending date");
        }

        if (DateUtils.getDaysBetween(startingDate, endingDate) > 3) {
            throw new IllegalArgumentException("The maximum reservation period is 3 days");
        }

        LocalDate now = LocalDate.now(clock);
        if (DateUtils.getDaysBetween(now, startingDate) < 1) {
            throw new IllegalArgumentException("The reservation must be at least 1 day ahead");
        }
        // TODO validate reserve is up to 1 month in advance
    }
}
