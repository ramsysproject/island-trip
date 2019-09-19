package com.emramirez.islandtrip.web;

import com.emramirez.islandtrip.model.Reservation;
import com.emramirez.islandtrip.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        try {
            Reservation result = reservationService.book(reservation);
            // TODO implement HATEOAS
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The booked date range is no longer available", ex);
        }
    }
}
