package com.emramirez.islandtrip.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
public class Reservation {

    @Id
    @GeneratedValue
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @Version
    private Long version;

    @Column
    @NotNull(message = "Please provide the arrival date")
    private LocalDate arrivalDate;

    @Column
    @NotNull(message = "Please provide the departure date")
    private LocalDate departureDate;

    @Column
    @NotNull(message = "Please provide the customer email")
    private String customerEmail;

    @Column
    @NotNull(message = "Please provide the customer name")
    private String customerName;

    @Enumerated(value = EnumType.STRING)
    private ReservationStatus status;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CalendarDate> calendarDates;
}
