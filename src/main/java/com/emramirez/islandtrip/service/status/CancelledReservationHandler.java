package com.emramirez.islandtrip.service.status;

import com.emramirez.islandtrip.model.Reservation;
import com.emramirez.islandtrip.model.ReservationStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CancelledReservationHandler implements ReservationStatusHandler {

    @Override
    public ReservationStatus supports() {
        return ReservationStatus.CANCELLED;
    }

    @Override
    public void accept(Reservation reservation) {
        reservation.setCalendarDates(Collections.emptySet());
        reservation.setStatus(this.supports());
    }
}
