package com.finartz.homework.TicketService.dto.request;

import com.finartz.homework.TicketService.domain.Passanger;
import com.finartz.homework.TicketService.util.FlightClass;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
    @Pattern(regexp="[0-9]+",message = "No yalnızca sayı icermelidir")
    private String no;
}
