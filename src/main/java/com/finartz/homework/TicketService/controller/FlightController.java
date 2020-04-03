package com.finartz.homework.TicketService.controller;

import com.finartz.homework.TicketService.dto.request.FlightRequestDTO;
import com.finartz.homework.TicketService.dto.request.TicketRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirportResponseDTO;
import com.finartz.homework.TicketService.dto.response.FlightResponseDTO;
import com.finartz.homework.TicketService.dto.response.wrapper.FlightsResponseDTO;
import com.finartz.homework.TicketService.exception.exception.ApiException;
import com.finartz.homework.TicketService.exception.exception.ArrivalBeforeDepartureException;
import com.finartz.homework.TicketService.service.FlightService;
import com.finartz.homework.TicketService.util.SearchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/flight")
public class FlightController {
    @Autowired
    private FlightService flightService;

    /**Ekleme**/
    @PostMapping("/add")
    public ResponseEntity<FlightResponseDTO> saveFlight(@Valid @RequestBody FlightRequestDTO flightRequestDTO) throws ArrivalBeforeDepartureException, ApiException {
        return new ResponseEntity<>(flightService.saveFlight(flightRequestDTO), HttpStatus.OK);
    }

    /**Güncelle**/
    @PostMapping("/update/{id}")
    public ResponseEntity<FlightResponseDTO> updateFlight(@PathVariable String id, @RequestBody FlightRequestDTO flightDto) throws ApiException, ArrivalBeforeDepartureException {
        return new ResponseEntity<>(flightService.updateFlight(id,flightDto),HttpStatus.OK);
    }

    /**Sil**/
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteFlight(@PathVariable String id) throws ApiException {
        flightService.deleteFlight(id);
    }


    /**Hepsini cek**/
    @GetMapping("/all")
    public ResponseEntity<List<FlightResponseDTO>> getAll(){
        return new ResponseEntity<>(flightService.getAll(), HttpStatus.OK);
    }

    /**Id ile Arama**/
    @GetMapping("/{id}")
    public ResponseEntity<FlightResponseDTO> getFlight(@PathVariable String id){
        return new ResponseEntity<>(flightService.getFlight(id), HttpStatus.OK);
    }

    /**Havayolu İsmi ile Arama**/
    @GetMapping("/airline")
    public ResponseEntity<List<FlightResponseDTO>> getFlightsByAirline(@RequestParam(required = true,name = "name") String airlineName){
        return new ResponseEntity<>(flightService.getFlightsByAirlineName(airlineName), HttpStatus.OK);
    }

    /**Kalkış Havaalanına göre Arama(value'den kalkan ucuslar)**/
    @GetMapping("/departure")
    public ResponseEntity<List<FlightResponseDTO>> getFlightsByDeparture(@RequestParam(required = true,name = "type") SearchType searchType
                                                                    ,@RequestParam(required = true,name = "value") String nameOrCity){
       return new ResponseEntity<>(flightService.getFlightsByDeparture(searchType,nameOrCity),HttpStatus.OK);
    }

    /**Varış Havaalanına göre Arama(value'ye kalkan ucuslar)**/
    @GetMapping("/arrival")
    public ResponseEntity<List<FlightResponseDTO>> getFlightsByArrival(@RequestParam(required = true,name = "type") SearchType searchType
                                                                ,@RequestParam(required = true,name = "value") String nameOrCity){
        return new ResponseEntity<>(flightService.getFlightsByArrival(searchType,nameOrCity),HttpStatus.OK);
    }

    /**Kalkış Havaalanı ve İniş Havaalanına göre arama**/
    @GetMapping("/temp")
    public ResponseEntity<FlightsResponseDTO> getFlightsByDepartureAndArrival(@RequestParam(required = true,name = "type") SearchType searchType
                                                                        ,@RequestParam(required = true,name = "departure") String departure
                                                                        ,@RequestParam(required = true,name = "arrival") String arrival){
        return new ResponseEntity<>(flightService.getFlightsByDepartureAndArrival(searchType,departure,arrival),HttpStatus.OK);
    }

}
