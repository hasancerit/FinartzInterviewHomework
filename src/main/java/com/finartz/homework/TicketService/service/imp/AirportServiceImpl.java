package com.finartz.homework.TicketService.service.imp;

import com.finartz.homework.TicketService.domain.Airport;
import com.finartz.homework.TicketService.dto.request.AirportRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirportResponseDTO;
import com.finartz.homework.TicketService.exception.exception.ApiException;
import com.finartz.homework.TicketService.repositories.AirportRepository;
import com.finartz.homework.TicketService.service.AirportService;
import com.finartz.homework.TicketService.util.SearchType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AirportServiceImpl implements AirportService {
    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private ModelMapper modelMapper;

    /*Ekleme*/
    @Override
    public AirportResponseDTO saveAirport(AirportRequestDTO airportDto) throws ApiException {
        Airport airport = null;
        try {
            airport = airportRepository.save(modelMapper.map(airportDto,Airport.class));
        } catch (DataIntegrityViolationException e) {
            throw new ApiException("name is already taken.",airportDto.getClass(),
                    "name",airportDto.getName());
        }
        return modelMapper.map(airport,AirportResponseDTO.class);
    }

    @Override
    public List<AirportResponseDTO> getAll() {
        return airportListToAirpostDtoList(airportRepository.findAll());
    }


    /*İd İle Arama*/
    @Override
    public AirportResponseDTO getAirport(String id) {
        try{
            return modelMapper.map(airportRepository.findById(id).get(),AirportResponseDTO.class);
        }catch (NoSuchElementException ex){
            return null;
        }
    }

    @Override
    public List<AirportResponseDTO> getAirports(SearchType searchType, String nameOrCity) {
        if(searchType == SearchType.byname){        /*İsmi ile Arama*/
            List<Airport> airports = airportRepository.findByNameIsContainingIgnoreCase(nameOrCity);
            return airportListToAirpostDtoList(airports);
        }else if(searchType == SearchType.bycity){  /*Şehiri ile Arama*/
            List<Airport> airports = airportRepository.findByCityIsContainingIgnoreCase(nameOrCity);
            return airportListToAirpostDtoList(airports);
        }else{                                      /*Şehiri VEYA ismi ile Arama*/
            List<Airport> airports = airportRepository.findByNameOrCity(nameOrCity,nameOrCity);
            return airportListToAirpostDtoList(airports);
        }
    }

    private List<AirportResponseDTO> airportListToAirpostDtoList(List<Airport> airports){
        List<AirportResponseDTO> airportResponseDTOList =new ArrayList<>();
        airports.stream().forEach(airport -> {
            airportResponseDTOList.add(modelMapper.map(airport,AirportResponseDTO.class));
        });
        return airportResponseDTOList;
    }
}
