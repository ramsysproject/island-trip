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
        LocalDate arrivalDate = reservation.getArrivalDate();
        LocalDate departureDate = reservation.getDepartureDate();
        Objects.requireNonNull(arrivalDate, "The reservation arrival date cannot be null");
        Objects.requireNonNull(departureDate, "The reservation departure date cannot be null");

        if (arrivalDate.isAfter(departureDate)) {
            throw new IllegalArgumentException("The reservation arrival date cannot be after departure date");
        }

        if (DateUtils.getDaysBetween(arrivalDate, departureDate) > 3) {
            throw new IllegalArgumentException("The maximum reservation period is 3 days");
        }

        LocalDate now = LocalDate.now(clock);
        if (DateUtils.getDaysBetween(now, arrivalDate) < 1) {
            throw new IllegalArgumentException("The reservation must be at least 1 day ahead of arrival");
        }

        if (DateUtils.getDaysBetween(now, arrivalDate) > 30) {
            throw new IllegalArgumentException("The reservation can be placed up to 30 days in advance");
        }
    }
}
