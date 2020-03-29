package com.finartz.homework.TicketService.service;

import com.finartz.homework.TicketService.domain.Flight;
import com.finartz.homework.TicketService.dto.request.FlightRequestDTO;
import com.finartz.homework.TicketService.dto.response.FlightResponseDTO;
import com.finartz.homework.TicketService.dto.response.FlightsResponseDTO;
import com.finartz.homework.TicketService.exception.exception.ArrivalBeforeDepartureException;
import com.finartz.homework.TicketService.util.SearchType;

import java.util.List;

public interface FlightService {
    FlightResponseDTO saveFlight(FlightRequestDTO flightDto) throws ArrivalBeforeDepartureException;

    List<FlightResponseDTO> getAll();

    FlightResponseDTO getFlight(String id);

    List<FlightResponseDTO> getFlightsByAirlineName(String airlineName);

    List<FlightResponseDTO> getFlightsByDeparture(SearchType searchType,String nameOrCity);

    List<FlightResponseDTO> getFlightsByArrival(SearchType searchType,String nameOrCity);

    FlightsResponseDTO  getFlightsByDepartureAndArrival(SearchType searchType, String departure, String arrival);
}
