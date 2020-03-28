package com.finartz.homework.TicketService.dto.request;

import com.finartz.homework.TicketService.domain.Passanger;
import com.finartz.homework.TicketService.util.FlightClass;
import lombok.Data;

@Data
public class TicketRequestDTO {
    private String flightId;
    private FlightClass flightClass;

    Passanger passanger;
    private String no;
}
