package com.finartz.homework.TicketService.service;

import com.finartz.homework.TicketService.domain.Airport;
import com.finartz.homework.TicketService.dto.request.AirportRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirportResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AirportService {
    AirportResponseDTO saveAirport(AirportRequestDTO airportDto);

    AirportResponseDTO getAirport(String id);

    List<AirportResponseDTO> getAirportsByCity(String city);

    List<AirportResponseDTO> getAirportsByName(String name);
}
