package com.emramirez.islandtrip.utils;

import com.emramirez.islandtrip.model.Reservation;

import java.time.LocalDate;

public class IntegrationTestUtils {

    public static Reservation buildReservation(
            LocalDate arrivalDate, LocalDate departureDate, String customerName, String email) {
        Reservation reservation = new Reservation();
        reservation.setArrivalDate(arrivalDate);
        reservation.setDepartureDate(departureDate);
        reservation.setCustomerName(customerName);
        reservation.setCustomerEmail(email);

        return reservation;
    }
}
