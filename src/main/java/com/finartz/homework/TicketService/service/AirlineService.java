package com.finartz.homework.TicketService.service;

import com.finartz.homework.TicketService.domain.Airline;
import com.finartz.homework.TicketService.dto.request.AirlineRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirlineResponseDTO;

public interface AirlineService {
    AirlineResponseDTO saveAirline(AirlineRequestDTO airlineDto);

    AirlineResponseDTO getAirline(String id);
}
