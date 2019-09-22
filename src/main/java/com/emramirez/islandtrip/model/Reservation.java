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

    @Version
    private Integer version;

    @Id
    @GeneratedValue
    private UUID id;

    @Column
    private LocalDate startingDate;

    @Column
    private LocalDate endingDate;

    @Enumerated(value = EnumType.STRING)
    private ReservationStatus status;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CalendarDate> calendarDates;
}
