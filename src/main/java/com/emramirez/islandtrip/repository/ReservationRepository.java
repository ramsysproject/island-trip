package com.emramirez.islandtrip.repository;

import com.emramirez.islandtrip.model.Reservation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    Reservation findById(UUID id);
}
