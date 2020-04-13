package com.finartz.homework.TicketService.service;

import com.finartz.homework.TicketService.dto.request.FlightRequestDTO;
import com.finartz.homework.TicketService.dto.response.FlightResponseDTO;
import com.finartz.homework.TicketService.dto.response.wrapper.FlightsResponseDTO;
import com.finartz.homework.TicketService.exception.exception.CustomAlreadyTaken;
import com.finartz.homework.TicketService.exception.exception.CustomNotFound;
import com.finartz.homework.TicketService.util.SearchType;

import java.util.List;

public interface FlightService {
    FlightResponseDTO saveFlight(FlightRequestDTO flightDto) throws  CustomAlreadyTaken, CustomNotFound;

    FlightResponseDTO updateFlight(String id, FlightRequestDTO flightDto) throws CustomAlreadyTaken, CustomNotFound;

    void  deleteFlight(String id) throws CustomNotFound;

    List<FlightResponseDTO> getAll();

    FlightResponseDTO getFlight(String id) throws CustomNotFound;

    List<FlightResponseDTO> getFlightsByAirlineName(String airlineName) throws CustomNotFound;

    List<FlightResponseDTO> getFlightsByDeparture(SearchType searchType,String nameOrCity) throws CustomNotFound;

    List<FlightResponseDTO> getFlightsByArrival(SearchType searchType,String nameOrCity) throws CustomNotFound;

    FlightsResponseDTO  getFlightsByDepartureAndArrival(SearchType searchType, String departure, String arrival) throws CustomNotFound;

}
