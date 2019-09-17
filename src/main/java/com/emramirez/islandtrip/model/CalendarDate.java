package com.emramirez.islandtrip.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@Entity
@EqualsAndHashCode(exclude = {"id"})
public class CalendarDate {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @Column
    private LocalDate calendarDate;
}
