package com.finartz.homework.TicketService.dto.request;

import com.finartz.homework.TicketService.util.FlightClass;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class TicketRequestDTO {
    private String userId;

    private String flightId;

    private FlightClass flightClass;
    private String no;
}
