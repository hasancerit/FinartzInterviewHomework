package com.finartz.homework.TicketService.service;

import com.finartz.homework.TicketService.dto.request.AirportRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirportResponseDTO;
import com.finartz.homework.TicketService.exception.exception.ApiException;
import com.finartz.homework.TicketService.util.SearchType;

import java.util.List;

public interface AirportService {
    AirportResponseDTO saveAirport(AirportRequestDTO airportDto) throws ApiException;

    AirportResponseDTO getAirport(String id);

    List<AirportResponseDTO> getAirports(SearchType searchType, String nameOrCity);

    List<AirportResponseDTO> getAll();

    void deleteAirport(String id) throws ApiException;
}
