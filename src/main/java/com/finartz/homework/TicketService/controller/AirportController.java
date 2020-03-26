package com.finartz.homework.TicketService.controller;

import com.finartz.homework.TicketService.domain.Airport;
import com.finartz.homework.TicketService.dto.request.AirportRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirlineResponseDTO;
import com.finartz.homework.TicketService.dto.response.AirportResponseDTO;
import com.finartz.homework.TicketService.service.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/airport")
public class AirportController {
    @Autowired
    AirportService airportService;

    @PostMapping("/add")
    public ResponseEntity<AirportResponseDTO> saveAirport(@RequestBody AirportRequestDTO airportDto){
        return new ResponseEntity<>(airportService.saveAirport(airportDto), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AirportResponseDTO> saveAirport(@PathVariable String id){
        return new ResponseEntity<>(airportService.getAirport(id), HttpStatus.OK);
    }

}
