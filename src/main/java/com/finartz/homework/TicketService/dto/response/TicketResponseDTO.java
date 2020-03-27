package com.finartz.homework.TicketService.dto.response;

import com.finartz.homework.TicketService.domain.Flight;
import com.finartz.homework.TicketService.domain.User;
import com.finartz.homework.TicketService.util.FlightClass;
import lombok.Data;

@Data
public class TicketResponseDTO {
    private String id;
    private String ticketNo;

    private User user;
    private FlightResponseDTO flight;
    private FlightClass flightClass;
    private String no;
}
