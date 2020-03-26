package com.finartz.homework.TicketService.controller;

import com.finartz.homework.TicketService.domain.Airline;
import com.finartz.homework.TicketService.domain.Flight;
import com.finartz.homework.TicketService.dto.request.AirlineRequestDTO;
import com.finartz.homework.TicketService.dto.request.FlightRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirportResponseDTO;
import com.finartz.homework.TicketService.dto.response.FlightResponseDTO;
import com.finartz.homework.TicketService.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/flight")
public class FlightController {
    @Autowired
    private FlightService flightService;

    @PostMapping("/add")
    public ResponseEntity<FlightResponseDTO> saveFlight(@RequestBody FlightRequestDTO flightRequestDTO){
        return new ResponseEntity<>(flightService.saveFlight(flightRequestDTO), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightResponseDTO> getFlight(@PathVariable String id){
        return new ResponseEntity<>(flightService.getFlight(id), HttpStatus.OK);
    }
}
