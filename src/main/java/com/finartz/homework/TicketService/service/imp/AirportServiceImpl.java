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

    /*Ekleme*/
    @Override
    public AirportResponseDTO saveAirport(AirportRequestDTO airportDto) {
        Airport airport = airportRepository.save(modelMapper.map(airportDto,Airport.class));
        return modelMapper.map(airport,AirportResponseDTO.class);
    }


    /*İd İle Arama*/
    @Override
    public AirportResponseDTO getAirport(String id) {
        Airport airport = airportRepository.getOne(id);
        return modelMapper.map(airport,AirportResponseDTO.class);
    }

    /*Şehir ile arama*/
    @Override
    public List<AirportResponseDTO> getAirportsByCity(String city) {
        List<Airport> airports = airportRepository.findByCityIsContainingIgnoreCase(city);
        return airportListToAirpostDtoList(airports);
    }

    /*İsim ile arama*/
    @Override
    public List<AirportResponseDTO> getAirportsByName(String name) {
        List<Airport> airports = airportRepository.findByNameIsContainingIgnoreCase(name);
        return airportListToAirpostDtoList(airports);
    }

    /*İsim veya şehir ile arama*/
    @Override
    public List<AirportResponseDTO> getAirportsByNameOrCity(String nameCity) {
        List<Airport> airports = airportRepository.findByNameIgnoreCaseIsContainingOrCityIgnoreCaseIsContaining(nameCity,nameCity);
        return airportListToAirpostDtoList(airports);
    }

    private List<AirportResponseDTO> airportListToAirpostDtoList(List<Airport> airports){
        List<AirportResponseDTO> airportResponseDTOList =new ArrayList<>();
        airports.stream().forEach(airport -> {
            airportResponseDTOList.add(modelMapper.map(airport,AirportResponseDTO.class));
        });
        return airportResponseDTOList;
    }
}
