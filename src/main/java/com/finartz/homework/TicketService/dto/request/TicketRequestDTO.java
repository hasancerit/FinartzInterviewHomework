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
    @NotBlank
    private String flightId;

    @NotNull
    private FlightClass flightClass;

    @Valid
    @NotNull
    Passanger passanger;
    private String no;
}
