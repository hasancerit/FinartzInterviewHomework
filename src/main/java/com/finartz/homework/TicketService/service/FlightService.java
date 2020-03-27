package com.finartz.homework.TicketService.service;

import com.finartz.homework.TicketService.domain.Flight;
import com.finartz.homework.TicketService.dto.request.FlightRequestDTO;
import com.finartz.homework.TicketService.dto.response.FlightResponseDTO;

import java.util.List;

public interface FlightService {
    FlightResponseDTO saveFlight(FlightRequestDTO flightDto);

    FlightResponseDTO getFlight(String id);

    List<FlightResponseDTO> getFlightsByAirlineName(String airlineName);

    List<FlightResponseDTO> getFlightsByDepartureName(String departureName);

    List<FlightResponseDTO> getFlightsByDepartureCity(String departureCity);

    List<FlightResponseDTO> getFlightsByDepartureNameOrCity(String departureNameOrCity);

    List<FlightResponseDTO> getFlightsByArrivalName(String arrivalName);

    List<FlightResponseDTO> getFlightsByArrivalCity(String arrivalCity);

    List<FlightResponseDTO> getFlightsByArrivalNameOrCity(String arrivalNameOrCity);
}
