package com.finartz.homework.TicketService.controller;

import com.finartz.homework.TicketService.dto.request.AirlineRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirlineResponseDTO;
import com.finartz.homework.TicketService.service.AirlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/airline")
public class AirlineController {
    @Autowired
    private AirlineService airlineService;

    /*Ekleme*/
    @PostMapping("/add")
    public ResponseEntity<AirlineResponseDTO> saveAirline(@RequestBody AirlineRequestDTO airlineRequestDTO){
        return new ResponseEntity<>(airlineService.saveAirline(airlineRequestDTO), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AirlineResponseDTO>> getAll(){
        return new ResponseEntity<>(airlineService.getAll(), HttpStatus.OK);
    }

    /*İd İle Arama*/
    @GetMapping("/{id}")
    public ResponseEntity<AirlineResponseDTO> getAirline(@PathVariable String id){
        return new ResponseEntity<>(airlineService.getAirline(id), HttpStatus.OK);
    }

    /*İsim ile arama*/
    @GetMapping("/name/{name}")
    public ResponseEntity<List<AirlineResponseDTO>> getAirlinesByName(@PathVariable String name){
        return new ResponseEntity<>(airlineService.getAirlinesByName(name), HttpStatus.OK);
    }
}
