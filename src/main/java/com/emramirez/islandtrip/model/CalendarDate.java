package com.emramirez.islandtrip.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@EqualsAndHashCode(exclude = {"id"})
public class CalendarDate {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private LocalDate calendarDate;

    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private CalendarDateStatus status;
}
