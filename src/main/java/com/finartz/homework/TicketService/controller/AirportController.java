package com.finartz.homework.TicketService.controller;

import com.finartz.homework.TicketService.domain.Airport;
import com.finartz.homework.TicketService.dto.request.AirportRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirlineResponseDTO;
import com.finartz.homework.TicketService.dto.response.AirportResponseDTO;
import com.finartz.homework.TicketService.service.AirportService;
import com.finartz.homework.TicketService.util.SearchType;
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

    @GetMapping("/search")
    public ResponseEntity<List<AirportResponseDTO>> getAirports(@RequestParam(required = true,name = "type") SearchType searchType
            ,@RequestParam(required = true,name = "value") String nameOrCity){
        return new ResponseEntity<>(airportService.getAirports(searchType,nameOrCity), HttpStatus.OK);
    }

    /**Sil Ve Güncelle**/
}
