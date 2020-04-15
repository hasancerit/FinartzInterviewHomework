package com.finartz.homework.TicketService.service;

import com.finartz.homework.TicketService.dto.request.AirlineRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirlineResponseDTO;
import com.finartz.homework.TicketService.exception.exception.CustomAlreadyTaken;
import com.finartz.homework.TicketService.exception.exception.CustomNotFound;
import org.springframework.http.ResponseEntity;

import javax.xml.ws.Response;
import java.util.List;

public interface AirlineService {
    AirlineResponseDTO saveAirline(AirlineRequestDTO airlineDto) throws CustomAlreadyTaken;

    AirlineResponseDTO updateAirline(String id, AirlineRequestDTO airlinetDto) throws CustomAlreadyTaken, CustomNotFound;

    void deleteAirline(String id) throws CustomNotFound;

    List<AirlineResponseDTO> getAll();

    AirlineResponseDTO getAirline(String id) throws CustomNotFound;

    List<AirlineResponseDTO> getAirlinesByName(String name) throws CustomNotFound;


}
