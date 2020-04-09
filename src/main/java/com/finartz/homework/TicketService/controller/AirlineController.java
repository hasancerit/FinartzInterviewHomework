package com.finartz.homework.TicketService.controller;

import com.finartz.homework.TicketService.config.SwaggerConfig;
import com.finartz.homework.TicketService.dto.request.AirlineRequestDTO;
import com.finartz.homework.TicketService.dto.request.FlightRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirlineResponseDTO;
import com.finartz.homework.TicketService.dto.response.FlightResponseDTO;
import com.finartz.homework.TicketService.exception.exception.ArrivalBeforeDepartureException;
import com.finartz.homework.TicketService.exception.exception.ApiException;
import com.finartz.homework.TicketService.service.AirlineService;
import com.finartz.homework.TicketService.service.FlightService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Api(tags = {SwaggerConfig.TAG_1 })
@RequestMapping("/airline")
public class AirlineController {
    private final AirlineService airlineService;
    private final FlightService flightService;


    /*Ekleme*/
    @ApiOperation(value = "saveAirline",notes = "This endpoint saves the successfully sent airline.")
    @PostMapping("/add")
    public ResponseEntity<AirlineResponseDTO> saveAirline(
            @ApiParam(value = "Airline Model to will be saved",required = true) @Valid @RequestBody AirlineRequestDTO airlineRequestDTO) throws ApiException {
        String a = "Hasan";
        return new ResponseEntity<>(airlineService.saveAirline(airlineRequestDTO), HttpStatus.OK);
    }

    /*Update*/
    @ApiOperation(value = "updateAirline",notes = "This endpoint updates the successfully sent airline.")
    @PostMapping("/update/{id}")
    public ResponseEntity<AirlineResponseDTO> updateAirline(
            @ApiParam(value = "Id of the airport to be updated.",required = true) @PathVariable String id,
            @ApiParam(value = "Airline Model to will be updated.",required = true) @RequestBody AirlineRequestDTO airlinetDto) throws ApiException {
        return new ResponseEntity<>(airlineService.updateAirline(id,airlinetDto),HttpStatus.OK);
    }

    /*Delete*/
    @DeleteMapping("/{id}")
    @ApiOperation(value = "deleteAirline",notes = "This endpoint deletes airline of the successfully sent Id.")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteAirline(
            @ApiParam(value = "Id of the airline to be deleted.",required = true) @PathVariable String id) throws ApiException {
        airlineService.deleteAirline(id);
    }


    @GetMapping("/all")
    @ApiOperation(value = "getAll",notes = "This endpoint serves all airlines.")
    public ResponseEntity<List<AirlineResponseDTO>> getAll(){
        return new ResponseEntity<>(airlineService.getAll(), HttpStatus.OK);
    }


    /*Id İle Arama*/
    @GetMapping("/{id}")
    @ApiOperation(value = "getAirline",notes = "This endpoint serves airline of the successfully sent Id.")
    public ResponseEntity<AirlineResponseDTO> getAirline(
            @ApiParam(value = "Id of the airline to be served.",required = true) @PathVariable String id){
        return new ResponseEntity<>(airlineService.getAirline(id), HttpStatus.OK);
    }

    /*Isim ile arama*/
    @GetMapping("/name")
    @ApiOperation(value = "getAirlinesByName",notes = "This endpoint serves airlines of the successfully sent name")
    public ResponseEntity<List<AirlineResponseDTO>> getAirlinesByName(
            @ApiParam(value = "Name of the airlines to be served.",required = true) @RequestParam(required = true,name = "name") String name){
        return new ResponseEntity<>(airlineService.getAirlinesByName(name), HttpStatus.OK);
    }

    /*Havayoluna ucus ekle*/
    @PostMapping("/{id}/addflight")
    @ApiOperation(value = "addFlightToAirline",notes = "This endpoint saves flight to airline of the successfully sent name")
    public ResponseEntity<FlightResponseDTO> addFlightToAirline(
            @ApiParam(value = "Id of the alirline to be saved new flight.",required = true) @PathVariable String id,
            @ApiParam(value = "Airline Model to will be saved",required = true) @Valid @RequestBody FlightRequestDTO flightRequestDTO) throws ArrivalBeforeDepartureException, ApiException {
        flightRequestDTO.setAirlineId(id);
        return new ResponseEntity<>(flightService.saveFlight(flightRequestDTO), HttpStatus.OK);
    }
}
