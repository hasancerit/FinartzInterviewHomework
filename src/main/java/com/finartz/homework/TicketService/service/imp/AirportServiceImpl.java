package com.finartz.homework.TicketService.service.imp;

import com.finartz.homework.TicketService.domain.Airport;
import com.finartz.homework.TicketService.dto.request.AirportRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirportResponseDTO;
import com.finartz.homework.TicketService.repositories.AirportRepository;
import com.finartz.homework.TicketService.service.AirportService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AirportServiceImpl implements AirportService {
    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AirportResponseDTO saveAirport(AirportRequestDTO airportDto) {
        Airport airport = airportRepository.save(modelMapper.map(airportDto,Airport.class));
        return modelMapper.map(airport,AirportResponseDTO.class);
    }

    @Override
    public AirportResponseDTO getAirport(String id) {
        Airport airport = airportRepository.getOne(id);
        return modelMapper.map(airport,AirportResponseDTO.class);
    }

    @Override
    public List<AirportResponseDTO> getAirportsByCity(String city) {
        List<Airport> airports = airportRepository.findByCityIsContainingIgnoreCase(city);
        List<AirportResponseDTO> airportResponseDTOList =new ArrayList<>();
        airports.stream().forEach(airport -> {
            airportResponseDTOList.add(modelMapper.map(airport,AirportResponseDTO.class));
        });
        return airportResponseDTOList;
    }

    @Override
    public List<AirportResponseDTO> getAirportsByName(String name) {
        List<Airport> airports = airportRepository.findByNameIsContainingIgnoreCase(name);
        List<AirportResponseDTO> airportResponseDTOList =new ArrayList<>();
        airports.stream().forEach(airport -> {
            airportResponseDTOList.add(modelMapper.map(airport,AirportResponseDTO.class));
        });
        return airportResponseDTOList;
    }
}
