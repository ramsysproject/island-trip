package com.emramirez.islandtrip.service.status;

import com.emramirez.islandtrip.model.Reservation;
import com.emramirez.islandtrip.model.ReservationStatus;

import java.util.function.Consumer;

public interface ReservationStatusHandler extends Consumer<Reservation> {

    /**
     * Returns which type of ${@link ReservationStatus} this handler supports.
     *
     * @return the reservation status which supports
     */
    ReservationStatus supports();
}
