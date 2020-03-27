package com.finartz.homework.TicketService.service.imp;

import com.finartz.homework.TicketService.domain.Airline;
import com.finartz.homework.TicketService.domain.Airport;
import com.finartz.homework.TicketService.dto.request.AirlineRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirlineResponseDTO;
import com.finartz.homework.TicketService.dto.response.AirportResponseDTO;
import com.finartz.homework.TicketService.repositories.AirlineRepository;
import com.finartz.homework.TicketService.service.AirlineService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AirlineServiceImpl implements AirlineService {
    @Autowired
    private AirlineRepository airlineRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AirlineResponseDTO saveAirline(AirlineRequestDTO airlineDto) {
        Airline airline = airlineRepository.save(modelMapper.map(airlineDto,Airline.class));
        return modelMapper.map(airline,AirlineResponseDTO.class);
    }

    @Override
    public AirlineResponseDTO getAirline(String id) {
        return modelMapper.map(airlineRepository.getOne(id),AirlineResponseDTO.class);
    }

    @Override
    public List<AirlineResponseDTO> getAirlinesByName(String name) {
        List<Airline> airports = airlineRepository.findByNameIsContainingIgnoreCase(name);
        List<AirlineResponseDTO> airlineResponseDTOList =new ArrayList<>();
        airports.stream().forEach(airline -> {
            airlineResponseDTOList.add(modelMapper.map(airline,AirlineResponseDTO.class));
        });
        return airlineResponseDTOList;
    }
}
