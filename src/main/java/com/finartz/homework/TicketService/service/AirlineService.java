package com.finartz.homework.TicketService.service;

import com.finartz.homework.TicketService.domain.Airline;
import com.finartz.homework.TicketService.dto.request.AirlineRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirlineResponseDTO;
import com.finartz.homework.TicketService.dto.response.AirportResponseDTO;

import java.util.List;

public interface AirlineService {
    AirlineResponseDTO saveAirline(AirlineRequestDTO airlineDto);

    AirlineResponseDTO getAirline(String id);

    List<AirlineResponseDTO> getAirlinesByName(String name);

    List<AirlineResponseDTO> getAll();

}
