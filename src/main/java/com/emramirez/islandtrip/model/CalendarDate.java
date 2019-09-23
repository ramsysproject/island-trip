package com.emramirez.islandtrip.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@EqualsAndHashCode()
public class CalendarDate {

    @Id
    @GeneratedValue
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(unique = true)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private CalendarDateStatus status;
}
