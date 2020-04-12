package com.finartz.homework.TicketService.service;

import com.finartz.homework.TicketService.dto.request.AirportRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirportResponseDTO;
import com.finartz.homework.TicketService.exception.exception.CustomAlreadyTaken;
import com.finartz.homework.TicketService.exception.exception.CustomNotFound;
import com.finartz.homework.TicketService.util.SearchType;

import java.util.List;

public interface AirportService {
    AirportResponseDTO saveAirport(AirportRequestDTO airportDto) throws CustomAlreadyTaken;

    AirportResponseDTO updateAirport(String id, AirportRequestDTO airportDto) throws CustomAlreadyTaken, CustomNotFound;

    void deleteAirport(String id) throws CustomAlreadyTaken, CustomNotFound;

    List<AirportResponseDTO> getAll();

    AirportResponseDTO getAirport(String id) throws CustomNotFound;

    List<AirportResponseDTO> getAirports(SearchType searchType, String nameOrCity) throws CustomNotFound;



}
