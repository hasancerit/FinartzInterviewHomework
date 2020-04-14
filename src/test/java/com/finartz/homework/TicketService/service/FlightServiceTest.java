package com.finartz.homework.TicketService.service;

import com.finartz.homework.TicketService.repositories.AirlineRepository;
import com.finartz.homework.TicketService.repositories.AirportRepository;
import com.finartz.homework.TicketService.repositories.FlightRepository;
import com.finartz.homework.TicketService.repositories.TicketRepository;
import com.finartz.homework.TicketService.service.imp.FlightServiceImpl;
import com.finartz.homework.TicketService.service.imp.TicketServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import static org.junit.jupiter.api.Assertions.*;

class FlightServiceTest {
    @Mock
    private FlightRepository flightRepository;
    @Mock
    private AirportRepository airportRepository;
    @Mock
    private AirlineRepository airlineRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private ModelMapper modelMapper;

    FlightServiceImpl flightService;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        flightService = new FlightServiceImpl(flightRepository,airportRepository,airlineRepository,ticketRepository,modelMapper);
    }

    @Test
    void saveFlight() {
    }

    @Test
    void updateFlight() {
    }

    @Test
    void deleteFlight() {
    }

    @Test
    void getAll() {
    }

    @Test
    void getFlight() {
    }

    @Test
    void getFlightsByAirlineName() {
    }

    @Test
    void getFlightsByDeparture() {
    }

    @Test
    void getFlightsByArrival() {
    }
}