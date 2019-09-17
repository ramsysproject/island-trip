package com.emramirez.islandtrip.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
public class Reservation {

    @Id
    @GeneratedValue
    private UUID id;

    @Column
    private LocalDate startingDate;

    @Column
    private LocalDate endingDate;
}
