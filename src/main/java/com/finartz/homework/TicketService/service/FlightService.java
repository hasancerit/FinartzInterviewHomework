package com.finartz.homework.TicketService.service;

import com.finartz.homework.TicketService.domain.Flight;
import com.finartz.homework.TicketService.dto.request.FlightRequestDTO;
import com.finartz.homework.TicketService.dto.response.FlightResponseDTO;

public interface FlightService {
    FlightResponseDTO saveFlight(FlightRequestDTO flightDto);

    FlightResponseDTO getFlight(String id);
}
