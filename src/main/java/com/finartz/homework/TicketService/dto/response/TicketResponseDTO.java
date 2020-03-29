package com.finartz.homework.TicketService.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.finartz.homework.TicketService.domain.Passanger;
import com.finartz.homework.TicketService.util.FlightClass;
import lombok.Data;

@Data
public class TicketResponseDTO {
    private String id;
    private String ticketNo;

    private Passanger passanger;

    @JsonIgnoreProperties({"seatsEconomic","seatsBusiness"})
    private FlightResponseDTO flight;
    private FlightClass flightClass;
    private String no;
}
