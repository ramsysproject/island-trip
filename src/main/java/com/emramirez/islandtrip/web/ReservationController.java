package com.emramirez.islandtrip.web;

import com.emramirez.islandtrip.dto.UpdateRequestDto;
import com.emramirez.islandtrip.exception.ValidationException;
import com.emramirez.islandtrip.model.Reservation;
import com.emramirez.islandtrip.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.UUID;

import static org.springframework.util.StringUtils.isEmpty;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    public static final String RANGE_UNAVAILABLE_MESSAGE = "The provided date range is no longer available for booking";
    public static final String RESOURCE_STATE_CHANGED = "The resource has been updated or deleted by another transaction";
    public static final String INVALID_ETAG_MESSAGE = "The provided entity tag is not longer valid";
    public static final String MISSING_ETAG_MESSAGE = "The If-Match request header must contain an eTag value";

    private final ReservationService reservationService;

    /**
     * Creates a new entity of type ${@link Reservation}.
     *
     * @param reservation
     * @return the new reservation, or an error code
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reservation> createReservation(@Valid @RequestBody Reservation reservation) {
        try {
            Reservation result = reservationService.book(reservation);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .eTag(String.valueOf(result.getVersion()))
                    .body(result);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, RANGE_UNAVAILABLE_MESSAGE, ex);
        } catch (ValidationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    /**
     * Attemps to modify a given ${@link Reservation} entity.
     *
     * @param request holds the eTag
     * @param id the resource id
     * @param updateRequestDto the dto which holds the fields to modify
     * @return the updated reservation, or an error code
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reservation> updateReservation(WebRequest request, @PathVariable UUID id,
                                                         @RequestBody UpdateRequestDto updateRequestDto) {
        Reservation currentReservation = reservationService.findById(id);
        if (currentReservation == null) {
            return ResponseEntity.notFound().build();
        }
        String ifMatchValue = request.getHeader("If-Match");
        if (isEmpty(ifMatchValue)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MISSING_ETAG_MESSAGE);
        }
        if (!ifMatchValue.equals(currentReservation.getVersion().toString())) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, INVALID_ETAG_MESSAGE);
        }

        try {
            Reservation result = reservationService.update(updateRequestDto, id);
            return ResponseEntity.ok()
                    .eTag(String.valueOf(result.getVersion()))
                    .body(result);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, RANGE_UNAVAILABLE_MESSAGE, ex);
        } catch (ObjectOptimisticLockingFailureException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, RESOURCE_STATE_CHANGED, ex);
        }
    }
}
