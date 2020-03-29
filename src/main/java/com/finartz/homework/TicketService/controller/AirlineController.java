package com.finartz.homework.TicketService.controller;

import com.finartz.homework.TicketService.dto.request.AirlineRequestDTO;
import com.finartz.homework.TicketService.dto.request.FlightRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirlineResponseDTO;
import com.finartz.homework.TicketService.dto.response.FlightResponseDTO;
import com.finartz.homework.TicketService.exception.exception.ArrivalBeforeDepartureException;
import com.finartz.homework.TicketService.service.AirlineService;
import com.finartz.homework.TicketService.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/airline")
public class AirlineController {
    @Autowired
    private AirlineService airlineService;

    @Autowired
    private FlightService flightService;

    /*Ekleme*/
    @PostMapping("/add")
    public ResponseEntity<AirlineResponseDTO> saveAirline(@Valid @RequestBody AirlineRequestDTO airlineRequestDTO){
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
    @GetMapping("/name")
    public ResponseEntity<List<AirlineResponseDTO>> getAirlinesByName(@RequestParam(required = true,name = "name") String name){
        return new ResponseEntity<>(airlineService.getAirlinesByName(name), HttpStatus.OK);
    }

    @PostMapping("/{id}/addflight")
    public ResponseEntity<FlightResponseDTO> addFlightToAirline(@Valid @RequestBody FlightRequestDTO flightRequestDTO,@PathVariable String id) throws ArrivalBeforeDepartureException {
        flightRequestDTO.setAirlineId(id);
        return new ResponseEntity<>(flightService.saveFlight(flightRequestDTO), HttpStatus.OK);
    }

    /**Sil Ve Güncelle**/
}
