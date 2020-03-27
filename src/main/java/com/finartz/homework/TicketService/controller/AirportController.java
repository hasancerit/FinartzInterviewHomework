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

import java.util.List;

@RestController
@RequestMapping("/airport")
public class AirportController {
    @Autowired
    AirportService airportService;

    /*Ekleme*/
    @PostMapping("/add")
    public ResponseEntity<AirportResponseDTO> saveAirport(@RequestBody AirportRequestDTO airportDto){
        return new ResponseEntity<>(airportService.saveAirport(airportDto), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AirportResponseDTO>> getAll(){
        return new ResponseEntity<>(airportService.getAll(), HttpStatus.OK);
    }


    /*İd İle Arama*/
    @GetMapping("/{id}")
    public ResponseEntity<AirportResponseDTO> getAirport(@PathVariable String id){
        return new ResponseEntity<>(airportService.getAirport(id), HttpStatus.OK);
    }

    /*Şehir ile arama*/
    @GetMapping("/city/{city}")
    public ResponseEntity<List<AirportResponseDTO>> getAirportsByCity(@PathVariable String city){
        return new ResponseEntity<>(airportService.getAirportsByCity(city), HttpStatus.OK);
    }

    /*İsim ile arama*/
    @GetMapping("/name/{name}")
    public ResponseEntity<List<AirportResponseDTO>> getAirportsByName(@PathVariable String name){
        return new ResponseEntity<>(airportService.getAirportsByName(name), HttpStatus.OK);
    }

    @GetMapping("/namecity/{nameorcity}")
    public ResponseEntity<List<AirportResponseDTO>> getAirportsByNameOrCity(@PathVariable("nameorcity") String nameCity){
        return new ResponseEntity<>(airportService.getAirportsByNameOrCity(nameCity), HttpStatus.OK);
    }
}
