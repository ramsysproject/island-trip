package com.emramirez.islandtrip.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;
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

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CalendarDate> calendarDates;
}
