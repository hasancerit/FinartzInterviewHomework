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

import java.util.List;

@RestController
@RequestMapping("/flight")
public class FlightController {
    @Autowired
    private FlightService flightService;

    /*Ekleme*/
    @PostMapping("/add")
    public ResponseEntity<FlightResponseDTO> saveFlight(@RequestBody FlightRequestDTO flightRequestDTO){
        return new ResponseEntity<>(flightService.saveFlight(flightRequestDTO), HttpStatus.OK);
    }


    /*Id ile Arama*/
    @GetMapping("/{id}")
    public ResponseEntity<FlightResponseDTO> getFlight(@PathVariable String id){
        return new ResponseEntity<>(flightService.getFlight(id), HttpStatus.OK);
    }


    /*Havayolu İsmi ile Arama*/
    @GetMapping("/airline/{airlinename}")
    public ResponseEntity<List<FlightResponseDTO>> getFlightsByAirline(@PathVariable("airlinename") String airlineName){
        return new ResponseEntity<>(flightService.getFlightsByAirlineName(airlineName), HttpStatus.OK);
    }

    /**Kalkış Havaalanı Arama**/
    /*Kalkış Havaalanı İsmi ile Arama*/
    @GetMapping("/departurecity/{departurecity}")
    public ResponseEntity<List<FlightResponseDTO>> getFlightsByDepartureCity(@PathVariable("departurecity") String departureCity){
        return new ResponseEntity<>(flightService.getFlightsByDepartureCity(departureCity), HttpStatus.OK);
    }

    /*Kalkış Havaalanı Şehiri ile Arama*/
    @GetMapping("/departurename/{departurename}")
    public ResponseEntity<List<FlightResponseDTO>> getFlightsByDepartureName(@PathVariable("departurename") String departureName){
        return new ResponseEntity<>(flightService.getFlightsByDepartureName(departureName), HttpStatus.OK);
    }

    /*Kalkış Havaalanı Şehiri VEYA ismi ile Arama*/
    @GetMapping("/departurenamecity/{departurenameorcity}")
    public ResponseEntity<List<FlightResponseDTO>> getFlightsByDepartureNameOrCity(@PathVariable("departurenameorcity") String departureNameOrCity){
        return new ResponseEntity<>(flightService.getFlightsByDepartureNameOrCity(departureNameOrCity), HttpStatus.OK);
    }


    /**Varış Havaalanı Arama**/
    /*Varış Havaalanı İsmi ile Arama*/
    @GetMapping("/arrivalcity/{arrivalcity}")
    public ResponseEntity<List<FlightResponseDTO>> getFlightsByArrivalCity(@PathVariable("arrivalcity") String arrivalCity){
        return new ResponseEntity<>(flightService.getFlightsByArrivalCity(arrivalCity), HttpStatus.OK);
    }

    /*Varış Havaalanı Şehiri ile Arama*/
    @GetMapping("/arrivalname/{arrivalname}")
    public ResponseEntity<List<FlightResponseDTO>> getFlightsByArrivalName(@PathVariable("arrivalname") String arrivalName){
        return new ResponseEntity<>(flightService.getFlightsByArrivalName(arrivalName), HttpStatus.OK);
    }

    /*Varış Havaalanı Şehiri VEYA ismi ile Arama*/
    @GetMapping("/arrivalnamecity/{arrivalnameorcity}")
    public ResponseEntity<List<FlightResponseDTO>> getFlightsByArrivalNameOrCity(@PathVariable("arrivalnameorcity") String arrivalNameOrCity){
        return new ResponseEntity<>(flightService.getFlightsByArrivalNameOrCity(arrivalNameOrCity), HttpStatus.OK);
    }
}
