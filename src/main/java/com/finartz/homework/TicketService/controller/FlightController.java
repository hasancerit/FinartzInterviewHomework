package com.finartz.homework.TicketService.controller;

import com.finartz.homework.TicketService.config.SwaggerConfig;
import com.finartz.homework.TicketService.dto.request.FlightRequestDTO;
import com.finartz.homework.TicketService.dto.response.FlightResponseDTO;
import com.finartz.homework.TicketService.dto.response.wrapper.FlightsResponseDTO;
import com.finartz.homework.TicketService.exception.exception.ApiException;
import com.finartz.homework.TicketService.exception.exception.ArrivalBeforeDepartureException;
import com.finartz.homework.TicketService.service.FlightService;
import com.finartz.homework.TicketService.util.SearchType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Api(tags = {SwaggerConfig.TAG_3})
@RequestMapping("/flight")
public class FlightController {
    private final FlightService flightService;

    /**Ekleme**/
    @PostMapping("/add")
    @ApiOperation(value = "saveFlight",notes = "This endpoint saves the successfully sent flight.")
    public ResponseEntity<FlightResponseDTO> saveFlight(
            @ApiParam(value = "Flight Model to will be saved",required = true) @Valid @RequestBody FlightRequestDTO flightRequestDTO) throws ArrivalBeforeDepartureException, ApiException {
        return new ResponseEntity<>(flightService.saveFlight(flightRequestDTO), HttpStatus.OK);
    }

    /**Güncelle**/
    @ApiOperation(value = "updateFlight",notes = "This endpoint updates the successfully sent flight.")
    @PostMapping("/update/{id}")
    public ResponseEntity<FlightResponseDTO> updateFlight(
            @ApiParam(value = "Id of the flight to be updated.",required = true) @PathVariable String id,
            @ApiParam(value = "Flight Model to will be updated.",required = true) @RequestBody FlightRequestDTO flightDto) throws ApiException, ArrivalBeforeDepartureException {
        return new ResponseEntity<>(flightService.updateFlight(id,flightDto),HttpStatus.OK);
    }

    /**Sil**/
    @DeleteMapping("/{id}")
    @ApiOperation(value = "deleteFlight",notes = "This endpoint deletes flight of the successfully sent Id.")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteFlight(
            @ApiParam(value = "Id of the flight to be deleted.",required = true) @PathVariable String id) throws ApiException {
        flightService.deleteFlight(id);
    }


    /**Hepsini cek**/
    @GetMapping("/all")
    @ApiOperation(value = "getAll",notes = "This endpoint serves all flights.")
    public ResponseEntity<List<FlightResponseDTO>> getAll(){
        return new ResponseEntity<>(flightService.getAll(), HttpStatus.OK);
    }

    /**Id ile Arama**/
    @GetMapping("/{id}")
    @ApiOperation(value = "getFlight",notes = "This endpoint serves flight of the successfully sent Id.")
    public ResponseEntity<FlightResponseDTO> getFlight(
            @ApiParam(value = "Id of the flight to be served.",required = true) @PathVariable String id){
        return new ResponseEntity<>(flightService.getFlight(id), HttpStatus.OK);
    }

    /**Havayolu İsmi ile Arama**/
    @GetMapping("/airline")
    @ApiOperation(value = "getAirport",notes = "This endpoint serves airport of the successfully airline name.")
    public ResponseEntity<List<FlightResponseDTO>> getFlightsByAirline(
            @ApiParam(value = "Search Value.",required = true) @RequestParam(required = true,name = "name") String airlineName){
        return new ResponseEntity<>(flightService.getFlightsByAirlineName(airlineName), HttpStatus.OK);
    }

    /**Kalkış Havaalanına göre Arama(value'den kalkan ucuslar)**/
    @GetMapping("/departure")
    @ApiOperation(value = "getFlightsByDeparture",notes = "This endpoint serves the flights by departure airports according to the search parameter sent.")
    public ResponseEntity<List<FlightResponseDTO>> getFlightsByDeparture(
            @ApiParam(value = "Search Parameter.",required = true) @RequestParam(required = true,name = "type") SearchType searchType,
            @ApiParam(value = "Search Value.",required = true) @RequestParam(required = true,name = "value") String nameOrCity){
       return new ResponseEntity<>(flightService.getFlightsByDeparture(searchType,nameOrCity),HttpStatus.OK);
    }

    /**Varış Havaalanına göre Arama(value'ye kalkan ucuslar)**/
    @GetMapping("/arrival")
    @ApiOperation(value = "getFlightsByArrival",notes = "This endpoint serves the flights by arrival airports according to the search parameter sent.")
    public ResponseEntity<List<FlightResponseDTO>> getFlightsByArrival(
            @ApiParam(value = "Search Parameter.",required = true) @RequestParam(required = true,name = "type") SearchType searchType,
            @ApiParam(value = "Search Value.",required = true) @RequestParam(required = true,name = "value") String nameOrCity){
        return new ResponseEntity<>(flightService.getFlightsByArrival(searchType,nameOrCity),HttpStatus.OK);
    }

    /**Kalkış Havaalanı ve İniş Havaalanına göre arama**/
    @GetMapping("/temp")
    @ApiOperation(value = "getFlightsByDepartureAndArrival",notes = "This endpoint serves the flights by arrival and departure airports according to the search parameter sent.")
    public ResponseEntity<FlightsResponseDTO> getFlightsByDepartureAndArrival(
            @ApiParam(value = "Search Parameter.",required = true) @RequestParam(required = true,name = "type") SearchType searchType,
            @ApiParam(value = "Search Value.",required = true) @RequestParam(required = true,name = "departure") String departure,
            @ApiParam(value = "Search Value.",required = true) @RequestParam(required = true,name = "arrival") String arrival){
        return new ResponseEntity<>(flightService.getFlightsByDepartureAndArrival(searchType,departure,arrival),HttpStatus.OK);
    }

}
