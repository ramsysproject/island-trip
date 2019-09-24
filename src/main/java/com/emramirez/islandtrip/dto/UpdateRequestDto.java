package com.emramirez.islandtrip.dto;

import com.emramirez.islandtrip.model.ReservationStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class UpdateRequestDto {

    @NotNull(message = "Please provide the arrival date")
    private LocalDate arrivalDate;

    @NotNull(message = "Please provide the departure date")
    private LocalDate departureDate;

    @NotNull(message = "Please provide the customer email")
    private String customerEmail;

    @NotNull(message = "Please provide the customer name")
    private String customerName;

    @NotNull(message = "Please provide the reservation status")
    private ReservationStatus status;
}
