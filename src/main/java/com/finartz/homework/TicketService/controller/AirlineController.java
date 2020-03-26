package com.finartz.homework.TicketService.controller;

import com.finartz.homework.TicketService.domain.Airline;
import com.finartz.homework.TicketService.dto.request.AirlineRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirlineResponseDTO;
import com.finartz.homework.TicketService.service.AirlineService;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;

@RestController
@RequestMapping("/airline")
public class AirlineController {
    @Autowired
    private AirlineService airlineService;

    @PostMapping("/add")
    public ResponseEntity<AirlineResponseDTO> saveAirline(@RequestBody AirlineRequestDTO airlineRequestDTO){
        return new ResponseEntity<>(airlineService.saveAirline(airlineRequestDTO), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AirlineResponseDTO> saveAirline(@PathVariable String id){
        return new ResponseEntity<>(airlineService.getAirline(id), HttpStatus.OK);
    }
}
