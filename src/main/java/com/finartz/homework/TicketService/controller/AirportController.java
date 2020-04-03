package com.finartz.homework.TicketService.controller;

import com.finartz.homework.TicketService.dto.request.AirlineRequestDTO;
import com.finartz.homework.TicketService.dto.request.AirportRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirportResponseDTO;
import com.finartz.homework.TicketService.exception.exception.ApiException;
import com.finartz.homework.TicketService.service.AirportService;
import com.finartz.homework.TicketService.util.SearchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/airport")
public class AirportController {
    @Autowired
    AirportService airportService;

    /*Ekleme*/
    @PostMapping("/add")
    public ResponseEntity<AirportResponseDTO> saveAirport(@Valid @RequestBody AirportRequestDTO airportDto) throws ApiException {
        return new ResponseEntity<>(airportService.saveAirport(airportDto), HttpStatus.OK);
    }

    /*Update*/
    @PostMapping("/update/{id}")
    public ResponseEntity<AirportResponseDTO> updateAirport(@PathVariable String id,@RequestBody AirportRequestDTO airportDto) throws ApiException {
        return new ResponseEntity<>( airportService.updateAirport(id,airportDto),HttpStatus.OK);
    }

    /*Silme*/
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteAirport(@PathVariable String id) throws ApiException {
        airportService.deleteAirport(id);
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

    /*Name-City ile arama*/
    @GetMapping("/search")
    public ResponseEntity<List<AirportResponseDTO>> getAirports(@RequestParam(required = true,name = "type") SearchType searchType
            ,@RequestParam(required = true,name = "value") String nameOrCity){
        return new ResponseEntity<>(airportService.getAirports(searchType,nameOrCity), HttpStatus.OK);
    }
}
