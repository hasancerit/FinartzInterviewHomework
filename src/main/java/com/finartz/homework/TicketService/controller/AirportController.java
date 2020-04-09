package com.finartz.homework.TicketService.controller;

import com.finartz.homework.TicketService.config.SwaggerConfig;
import com.finartz.homework.TicketService.dto.request.AirportRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirportResponseDTO;
import com.finartz.homework.TicketService.exception.exception.ApiException;
import com.finartz.homework.TicketService.service.AirportService;
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
@Api(tags = {SwaggerConfig.TAG_2})
@RequestMapping("/airport")
public class AirportController {
    private final AirportService airportService;

    /*Ekleme*/
    @ApiOperation(value = "saveAirport",notes = "This endpoint saves the successfully sent airport.")
    @PostMapping("/add")
    public ResponseEntity<AirportResponseDTO> saveAirport(
            @ApiParam(value = "Airport Model to will be saved",required = true) @Valid @RequestBody AirportRequestDTO airportDto) throws ApiException {
        return new ResponseEntity<>(airportService.saveAirport(airportDto), HttpStatus.OK);
    }

    /*Update*/
    @ApiOperation(value = "updateAirport",notes = "This endpoint updates the successfully sent airport.")
    @PostMapping("/update/{id}")
    public ResponseEntity<AirportResponseDTO> updateAirport(
            @ApiParam(value = "Id of the airport to be updated.",required = true) @PathVariable String id,
            @ApiParam(value = "Airport Model to will be updated.",required = true) @RequestBody AirportRequestDTO airportDto) throws ApiException {
        return new ResponseEntity<>( airportService.updateAirport(id,airportDto),HttpStatus.OK);
    }

    /*Silme*/
    @DeleteMapping("/{id}")
    @ApiOperation(value = "deleteAirport",notes = "This endpoint deletes airport of the successfully sent Id.")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteAirport(
            @ApiParam(value = "Id of the airport to be deleted.",required = true) @PathVariable String id) throws ApiException {
        airportService.deleteAirport(id);
    }


    @GetMapping("/all")
    @ApiOperation(value = "getAll",notes = "This endpoint serves all airports.")
    public ResponseEntity<List<AirportResponseDTO>> getAll(){
        return new ResponseEntity<>(airportService.getAll(), HttpStatus.OK);
    }

    /*İd İle Arama*/
    @GetMapping("/{id}")
    @ApiOperation(value = "getAirport",notes = "This endpoint serves airport of the successfully sent Id.")
    public ResponseEntity<AirportResponseDTO> getAirport(
            @ApiParam(value = "Id of the airport to be served.",required = true) @PathVariable String id){
        return new ResponseEntity<>(airportService.getAirport(id), HttpStatus.OK);
    }

    /*Name-City ile arama*/
    @GetMapping("/search")
    @ApiOperation(value = "getAirports",notes = "This endpoint serves the airports according to the search parameter sent.")
    public ResponseEntity<List<AirportResponseDTO>> getAirports(
            @ApiParam(value = "Search Parameter.",required = true) @RequestParam(required = true,name = "type") SearchType searchType,
            @ApiParam(value = "Search Value.",required = true) @RequestParam(required = true,name = "value") String nameOrCity){
        return new ResponseEntity<>(airportService.getAirports(searchType,nameOrCity), HttpStatus.OK);
    }
}
