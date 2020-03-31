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
    @NotBlank(message = "flightId cannot be null.")
    private String flightId;

    @NotNull(message = "flightClass cannot be null.")
    private FlightClass flightClass;

    @Valid
    @NotNull(message = "passanger cannot be null.")
    Passanger passanger;

    @NotBlank(message = "no cannot be null.")
    @Pattern(regexp="[0-9]+",message = "no must have only numbers.")
    private String no;
}
