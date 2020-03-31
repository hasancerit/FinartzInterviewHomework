package com.finartz.homework.TicketService.service.imp;

import com.finartz.homework.TicketService.domain.Airline;
import com.finartz.homework.TicketService.dto.request.AirlineRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirlineResponseDTO;
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
        Airline airline = null;
        try {
            airline = airlineRepository.save(modelMapper.map(airlineDto,Airline.class));
        } catch (DataIntegrityViolationException e) {
            throw new ApiException("name is already taken.",airlineDto.getClass(),
                    "name",airlineDto.getName());
        }
        return modelMapper.map(airline,AirlineResponseDTO.class);
    }

    @Override
    public List<AirlineResponseDTO> getAll() {
        return airlineListToAirlineDtoList(airlineRepository.findAll());
    }

    /*İd İle Arama*/
    @Override
    public AirlineResponseDTO getAirline(String id) {
        try{
            return modelMapper.map(airlineRepository.findById(id).get(),AirlineResponseDTO.class);
        }catch (NoSuchElementException ex){
            return null;
        }
    }

    /*İsim ile arama*/
    @Override
    public List<AirlineResponseDTO> getAirlinesByName(String name) {
        return airlineListToAirlineDtoList(airlineRepository.findByNameIsContainingIgnoreCase(name));
    }


    private List<AirlineResponseDTO> airlineListToAirlineDtoList(List<Airline> airlines){
        List<AirlineResponseDTO> airlineResponseDTOList =new ArrayList<>();
        airlines.stream().forEach(airline -> {
            airlineResponseDTOList.add(modelMapper.map(airline,AirlineResponseDTO.class));
        });
        return airlineResponseDTOList;
    }
}
