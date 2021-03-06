package com.finartz.homework.TicketService.controller;

import com.finartz.homework.TicketService.config.SwaggerConfig;
import com.finartz.homework.TicketService.dto.request.AirportRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirportResponseDTO;
import com.finartz.homework.TicketService.exception.exception.CustomAlreadyTaken;
import com.finartz.homework.TicketService.exception.exception.CustomNotFound;
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

    /**
     * Ekleme
     *
     * @param airportReqDto Kaydedilecek Airport'un modeli
     * @return              Kaydedilen Airport'un modeli
     * @throws CustomAlreadyTaken Airport Name zaten varsa
     */
    @PostMapping("/add")
    @ApiOperation(value = "Save Airport",notes = "This endpoint saves the successfully sent airport.")
    public ResponseEntity<AirportResponseDTO> saveAirport(
            @ApiParam(value = "Airport Model to will be saved",required = true) @Valid @RequestBody AirportRequestDTO airportReqDto) throws CustomAlreadyTaken {
        return new ResponseEntity<>(airportService.saveAirport(airportReqDto), HttpStatus.OK);
    }

    /**
     * Guncelleme
     *
     * @param id            Guncellenecek Airport id'si
     * @param airportReqDto Guncellenecek Airport'un yeni alanları
     * @return              Guncellenen Airport'un modeli
     * @throws CustomNotFound     Airport id bulunamazsa
     * @throws CustomAlreadyTaken Airport name zaten alinmissa
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "Update Airport",notes = "This endpoint updates the successfully sent airport.")
    public ResponseEntity<AirportResponseDTO> updateAirport(
            @ApiParam(value = "Id of the airport to be updated.",required = true) @PathVariable String id,
            @ApiParam(value = "Airport Model to will be updated.",required = true)@Valid @RequestBody AirportRequestDTO airportReqDto) throws CustomAlreadyTaken, CustomNotFound {
        return new ResponseEntity<>( airportService.updateAirport(id,airportReqDto),HttpStatus.OK);
    }

    /**
     * Silme
     *
     * @param id            Silinecek Airport id'si
     * @throws CustomNotFound   Airport id bulunamazsa
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete Airport",notes = "This endpoint deletes airport of the successfully sent Id.")
    public ResponseEntity<String> deleteAirport(
            @ApiParam(value = "Id of the airport to be deleted.",required = true) @PathVariable String id) throws CustomNotFound {
        airportService.deleteAirport(id);
        return new ResponseEntity<>("\"Airport with id " + id + " has been deleted\"",HttpStatus.OK);
    }

    /**
     * Hepsini cek
     *
     * @return Veritabanındaki tüm Airport'lar.
     */
    @GetMapping("/all")
    @ApiOperation(value = "Get All Airports",notes = "This endpoint serves all airports.")
    public ResponseEntity<List<AirportResponseDTO>> getAllAirports(){
        return new ResponseEntity<>(airportService.getAll(), HttpStatus.OK);
    }

    /**
     * Id İle Arama
     *
     * @param id                Alinmak istenen Airport id'si
     * @return                  Istenen Airport modeli
     * @throws CustomNotFound   Airport id bulunamazsa
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "Get Airport By Id",notes = "This endpoint serves airport of the successfully sent Id.")
    public ResponseEntity<AirportResponseDTO> getAirportById(
            @ApiParam(value = "Id of the airport to be served.",required = true) @PathVariable String id) throws CustomNotFound {
        return new ResponseEntity<>(airportService.getAirport(id),HttpStatus.OK);
    }

    /**
     * Name-City ile arama - Airportun isimi veya Airportun bulundugu sehire göre arama
     *
     * @param searchType    Isime göre(byname) - sadece isime göre arar,
     *                      sehire göre(bycity) - sadece sehire göre arar,
     *                      isim veya sehire göre(bynameorcity) - hem isim hem sehir icinde arar,
     * @param nameOrCity    Aranacak kelime
     * @return              Bulunan Airport modelleri.
     * @throws CustomNotFound   Aranan degerde airport bulunamazsa
     */
    @GetMapping
    @ApiOperation(value = "Get Airports By Search Type",notes = "This endpoint serves the airports according to the search parameter sent.")
    public ResponseEntity<List<AirportResponseDTO>> getAirportsBySearchType(
            @ApiParam(value = "Search Parameter.",required = true) @RequestParam(required = true,name = "type") SearchType searchType,
            @ApiParam(value = "Search Value.",required = true) @RequestParam(required = true,name = "value") String nameOrCity) throws CustomNotFound {
        return new ResponseEntity<>(airportService.getAirports(searchType,nameOrCity),HttpStatus.OK);
    }
}
