package com.finartz.homework.TicketService.service;

import com.finartz.homework.TicketService.dto.request.AirlineRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirlineResponseDTO;
import com.finartz.homework.TicketService.exception.exception.ApiException;

import java.util.List;

public interface AirlineService {
    AirlineResponseDTO saveAirline(AirlineRequestDTO airlineDto) throws ApiException;

    AirlineResponseDTO updateAirline(String id, AirlineRequestDTO airlinetDto) throws ApiException;

    void deleteAirline(String id) throws ApiException;

    List<AirlineResponseDTO> getAll();

    AirlineResponseDTO getAirline(String id);

    List<AirlineResponseDTO> getAirlinesByName(String name);


}
