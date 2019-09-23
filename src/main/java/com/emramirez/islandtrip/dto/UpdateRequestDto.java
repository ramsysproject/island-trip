package com.emramirez.islandtrip.dto;

import com.emramirez.islandtrip.model.ReservationStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateRequestDto {
    private Long version;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
    private String customerEmail;
    private String customerName;
    private ReservationStatus status;
}
