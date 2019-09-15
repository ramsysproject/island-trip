package com.emramirez.islandtrip.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
public class Reservation {
    @Id
    private Long id;

    @Column
    private UUID uuid;

    @Column
    private LocalDate startingDate;

    @Column
    private LocalDate endingDate;
}
