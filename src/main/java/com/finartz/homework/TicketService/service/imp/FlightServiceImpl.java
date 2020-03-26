package com.finartz.homework.TicketService.service.imp;

import com.finartz.homework.TicketService.domain.Flight;
import com.finartz.homework.TicketService.dto.request.FlightRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirlineResponseDTO;
import com.finartz.homework.TicketService.dto.response.AirportResponseDTO;
import com.finartz.homework.TicketService.dto.response.FlightResponseDTO;
import com.finartz.homework.TicketService.repositories.AirlineRepository;
import com.finartz.homework.TicketService.repositories.AirportRepository;
import com.finartz.homework.TicketService.repositories.FlightRepository;
import com.finartz.homework.TicketService.service.AirlineService;
import com.finartz.homework.TicketService.service.AirportService;
import com.finartz.homework.TicketService.service.FlightService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlightServiceImpl implements FlightService {
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private AirportRepository airportRepository;
    @Autowired
    private AirlineRepository airlineRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public FlightResponseDTO saveFlight(FlightRequestDTO flightDto) {
        Flight flight = modelMapper.map(flightDto,Flight.class);

        /*Bunları ModelMapper Conf ile yapabilir miyim?*/
        flight.setDeparture(airportRepository.getOne(flightDto.getDepartureAirportId()));
        flight.setArrival(airportRepository.getOne(flightDto.getArrivalAirportId()));
        flight.setAirline(airlineRepository.getOne(flightDto.getAirlineId()));

        flightRepository.save(flight);
        return modelMapper.map(flight,FlightResponseDTO.class);
    }

    @Override
    public FlightResponseDTO getFlight(String id){
        return modelMapper.map(flightRepository.getOne(id),FlightResponseDTO.class);
    }
}
