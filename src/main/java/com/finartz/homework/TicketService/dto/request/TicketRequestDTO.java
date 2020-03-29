package com.finartz.homework.TicketService.dto.request;

import com.finartz.homework.TicketService.domain.Passanger;
import com.finartz.homework.TicketService.util.FlightClass;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class TicketRequestDTO {
    @NotBlank(message = "Flight Id Bos Birakilamaz.")
    private String flightId;

    @NotNull(message = "Flight Class Bos Birakilamaz.")
    private FlightClass flightClass;

    @Valid
    @NotNull(message = "Passanger Bos Birakilamaz.")
    Passanger passanger;

    @NotBlank(message = "No Bos Birakilamaz.")
    private String no;
}
