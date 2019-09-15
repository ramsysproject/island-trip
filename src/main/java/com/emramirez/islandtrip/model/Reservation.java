package com.emramirez.islandtrip.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class Reservation {
    private Long id;
    private UUID uuid;
    private LocalDate startingDate;
    private LocalDate endingDate;
}
