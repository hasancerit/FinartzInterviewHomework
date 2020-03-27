package com.finartz.homework.TicketService.service.imp;

import com.fasterxml.jackson.annotation.JsonView;
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

import java.util.ArrayList;
import java.util.List;

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



    @Override
    public List<FlightResponseDTO> getFlightsByAirlineName(String airlineName) {
        List<Flight> flights = flightRepository.findByAirline_NameIsContainingIgnoreCase(airlineName);
        return flightListToFlightDtoList(flights);
    }



    @Override
    public List<FlightResponseDTO> getFlightsByDepartureName(String departureName) {
        List<Flight> flights = flightRepository.findByDeparture_NameContainingIgnoreCase(departureName);
        return flightListToFlightDtoList(flights);
    }

    @Override
    public List<FlightResponseDTO> getFlightsByDepartureCity(String departureCity) {
        List<Flight> flights = flightRepository.findByDeparture_CityContainingIgnoreCase(departureCity);
        return flightListToFlightDtoList(flights);
    }

    @Override
    public List<FlightResponseDTO> getFlightsByDepartureNameOrCity(String departureNameOrCity) {
        List<Flight> flights = flightRepository.findByDeparture_CityContainingIgnoreCaseOrDeparture_NameContainingIgnoreCase(departureNameOrCity,departureNameOrCity);
        return flightListToFlightDtoList(flights);
    }



    @Override
    public List<FlightResponseDTO> getFlightsByArrivalName(String arrivalName) {
        List<Flight> flights = flightRepository.findByArrival_NameContainingIgnoreCase(arrivalName);
        return flightListToFlightDtoList(flights);
    }

    @Override
    public List<FlightResponseDTO> getFlightsByArrivalCity(String arrivalCity) {
        List<Flight> flights = flightRepository.findByArrival_CityContainingIgnoreCase(arrivalCity);
        return flightListToFlightDtoList(flights);
    }

    @Override
    public List<FlightResponseDTO> getFlightsByArrivalNameOrCity(String arrivalNameOrCity) {
        List<Flight> flights = flightRepository.findByArrival_CityContainingIgnoreCaseOrDeparture_NameContainingIgnoreCase(arrivalNameOrCity,arrivalNameOrCity);
        return flightListToFlightDtoList(flights);
    }

    private List<FlightResponseDTO> flightListToFlightDtoList(List<Flight> flights){
        List<FlightResponseDTO> flightResponseDTOList = new ArrayList<>();
        flights.stream().forEach(airline -> {
            flightResponseDTOList.add(modelMapper.map(airline,FlightResponseDTO.class));
        });
        return flightResponseDTOList;
    }
}
