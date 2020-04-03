package com.finartz.homework.TicketService.service.imp;

import com.finartz.homework.TicketService.domain.Airline;
import com.finartz.homework.TicketService.domain.Airport;
import com.finartz.homework.TicketService.domain.Flight;
import com.finartz.homework.TicketService.dto.request.AirlineRequestDTO;
import com.finartz.homework.TicketService.dto.request.AirportRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirlineResponseDTO;
import com.finartz.homework.TicketService.dto.response.AirportResponseDTO;
import com.finartz.homework.TicketService.exception.exception.ApiException;
import com.finartz.homework.TicketService.repositories.AirlineRepository;
import com.finartz.homework.TicketService.repositories.AirportRepository;
import com.finartz.homework.TicketService.service.AirlineService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AirlineServiceImpl implements AirlineService {
    @Autowired
    private AirlineRepository airlineRepository;
    @Autowired
    private AirportRepository airportRepository;
    @Autowired
    private ModelMapper modelMapper;

    /*Ekleme*/
    @Override
    public AirlineResponseDTO saveAirline(AirlineRequestDTO airlineDto) throws ApiException {
        Airline airline = handleSaveAirline(modelMapper.map(airlineDto, Airline.class), airlineDto);
        return modelMapper.map(airline, AirlineResponseDTO.class);
    }

    /*Update*/
    @Override
    public AirlineResponseDTO updateAirline(String id, AirlineRequestDTO airlineDto) throws ApiException {
        Airline airline = null;
        try {
            airline = airlineRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new ApiException("airportId Not Found", id.getClass(), "airportId", id);
        }

        airline.setName(airlineDto.getName());
        airline.setDesc(airlineDto.getDesc());

        return modelMapper.map(handleSaveAirline(airline, airlineDto), AirlineResponseDTO.class);
    }

    private Airline handleSaveAirline(Airline airline, AirlineRequestDTO airlineDto) throws ApiException {
        try {
            airline = airlineRepository.save(airline);
        } catch (DataIntegrityViolationException e) {
            throw new ApiException("name is already taken.", airlineDto.getClass(),
                    "name", airlineDto.getName());
        }
        return airline;
    }

    /*Silme*/
    @Override
    public void deleteAirline(String id) throws ApiException {
        Airline airline;
        try {
            airline = airlineRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new ApiException("airlineId Not Found", id.getClass(), "airlineId", id);
        }
        airlineRepository.delete(airline);
    }


    @Override
    public List<AirlineResponseDTO> getAll() {
        return airlineListToAirlineDtoList(airlineRepository.findAll());
    }

    /*İd İle Arama*/
    @Override
    public AirlineResponseDTO getAirline(String id) {
        try {
            return modelMapper.map(airlineRepository.findById(id).get(), AirlineResponseDTO.class);
        } catch (NoSuchElementException ex) {
            return null;
        }
    }

    /*İsim ile arama*/
    @Override
    public List<AirlineResponseDTO> getAirlinesByName(String name) {
        return airlineListToAirlineDtoList(airlineRepository.findByNameIsContainingIgnoreCase(name));
    }

    /*AirlineList'in icinde bulunan her Airline nesnesini, AirlineResponseDto nesnesine dönüstür*/
    private List<AirlineResponseDTO> airlineListToAirlineDtoList(List<Airline> airlines) {
        List<AirlineResponseDTO> airlineResponseDTOList = new ArrayList<>();
        airlines.stream().forEach(airline -> {
            airlineResponseDTOList.add(modelMapper.map(airline, AirlineResponseDTO.class));
        });
        return airlineResponseDTOList;
    }
}
