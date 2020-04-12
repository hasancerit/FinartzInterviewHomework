package com.finartz.homework.TicketService.controller;

import com.finartz.homework.TicketService.config.SwaggerConfig;
import com.finartz.homework.TicketService.dto.request.FlightRequestDTO;
import com.finartz.homework.TicketService.dto.response.FlightResponseDTO;
import com.finartz.homework.TicketService.dto.response.wrapper.FlightsResponseDTO;
import com.finartz.homework.TicketService.exception.exception.CustomAlreadyTaken;
import com.finartz.homework.TicketService.exception.exception.ArrivalBeforeDepartureException;
import com.finartz.homework.TicketService.exception.exception.CustomNotFound;
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

    /**Ekleme
     *
     * @param flightRequestDTO  Eklenecek flight
     * @return                  Eklenen flight
     * @throws CustomAlreadyTaken               Flight kalkis ve varis havaalani ayni sehirde ise
     * @throws ArrivalBeforeDepartureException  Eklenmek istenen flight kalkis ve inis saati kontrolu
     * @throws CustomNotFound                   AirlineId, DepartureId,ArrivalId bulunamaz ise
     */
    @PostMapping("/add")
    @ApiOperation(value = "saveFlight",notes = "This endpoint saves the successfully sent flight.")
    public ResponseEntity<FlightResponseDTO> saveFlight(
            @ApiParam(value = "Flight Model to will be saved",required = true) @Valid @RequestBody FlightRequestDTO flightRequestDTO)
            throws ArrivalBeforeDepartureException, CustomAlreadyTaken, CustomNotFound {
        return new ResponseEntity<>(flightService.saveFlight(flightRequestDTO), HttpStatus.OK);
    }

    /**
     * Havayoluna ucus ekle (FlightController'a taşınacak)
     *
     * @param id                Ucus eklenecek airline id'si
     * @param flightRequestDTO  Eklenecek ucus
     * @return                  Eklenen ucus
     * @throws CustomAlreadyTaken               Flight kalkis ve varis havaalani ayni sehirde ise
     * @throws ArrivalBeforeDepartureException  Eklenmek istenen flight kalkis ve inis saati kontrolu
     * @throws CustomNotFound                   AirlineId, DepartureId,ArrivalId bulunamaz ise
     */
    @PostMapping("/add/toairline/{airlineId}")
    @ApiOperation(value = "addFlightToAirline",notes = "This endpoint saves flight to airline of the successfully sent name")
    public ResponseEntity<FlightResponseDTO> addFlightToAirline(
            @ApiParam(value = "Id of the alirline to be saved new flight.",required = true) @PathVariable String airlineId,
            @ApiParam(value = "Airline Model to will be saved",required = true) @Valid @RequestBody FlightRequestDTO flightRequestDTO)
            throws ArrivalBeforeDepartureException, CustomAlreadyTaken, CustomNotFound {
        flightRequestDTO.setAirlineId(airlineId);
        return new ResponseEntity<>(flightService.saveFlight(flightRequestDTO), HttpStatus.OK);
    }

    /**
     * Guncelleme
     *
     * @param id            Guncellenecek flight id'si
     * @param flightDto     Guncellenecek flight'ın yeni alanları
     * @return              Guncellenen Flight'ın modeli
     * @throws CustomAlreadyTaken               Flight kalk havaalani ayni sehirde ise
     * @throws ArrivalBeforeDepartureException  Eklenmek istenen flight kalkis ve inis saati kontrolu
     * @throws CustomNotFound                   FlightId, AirlineId, DepartureId,ArrivalId bulunamaz ise
     */
    @ApiOperation(value = "updateFlight",notes = "This endpoint updates the successfully sent flight.")
    @PostMapping("/update/{id}")
    public ResponseEntity<FlightResponseDTO> updateFlight(
            @ApiParam(value = "Id of the flight to be updated.",required = true) @PathVariable String id,
            @ApiParam(value = "Flight Model to will be updated.",required = true)@Valid @RequestBody FlightRequestDTO flightDto)
            throws ArrivalBeforeDepartureException, CustomAlreadyTaken, CustomNotFound {
        return new ResponseEntity<>(flightService.updateFlight(id,flightDto),HttpStatus.OK);
    }

    /**
     * Sil
     *
     * @param id                Silinecek flight id'si
     * @throws CustomNotFound   Flight id bulunamazsa
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "deleteFlight",notes = "This endpoint deletes flight of the successfully sent Id.")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteFlight(
            @ApiParam(value = "Id of the flight to be deleted.",required = true) @PathVariable String id) throws CustomNotFound {
        flightService.deleteFlight(id);
    }


    /**
     * Hepsini cek
     *
     * @return Veritabanındaki tüm flightlar.
     */
    @GetMapping("/all")
    @ApiOperation(value = "getAll",notes = "This endpoint serves all flights.")
    public ResponseEntity<List<FlightResponseDTO>> getAll(){
        return new ResponseEntity<>(flightService.getAll(), HttpStatus.OK);
    }

    /**
     * Id ile Arama
     *
     * @param id     Alinmak istenen Airline id'si
     * @return       Istenen flight modeli - Id bulunmaz ise null döner.
     * @throws CustomNotFound   Flight id bulunamazsa
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "getFlight",notes = "This endpoint serves flight of the successfully sent Id.")
    public ResponseEntity<FlightResponseDTO> getFlight(
            @ApiParam(value = "Id of the flight to be served.",required = true) @PathVariable String id) throws CustomNotFound {
        return new ResponseEntity<>(flightService.getFlight(id), HttpStatus.OK);
    }

    /**
     * Arline Name ile Arama - Havayoluna ait aktif ucuslar.
     *
     * @param airlineName   Ucuslari cekilmek istenen Airline ismi.
     * @return              Istenen Flight modellari.
     * @throws CustomNotFound   Aranan airlineName bulunamazsa
     */
    @GetMapping("/airline")
    @ApiOperation(value = "getAirport",notes = "This endpoint serves airport of the successfully airline name.")
    public ResponseEntity<List<FlightResponseDTO>> getFlightsByAirline(
            @ApiParam(value = "Search Value.",required = true) @RequestParam(required = true,name = "name") String airlineName) throws CustomNotFound {
        return new ResponseEntity<>(flightService.getFlightsByAirlineName(airlineName), HttpStatus.OK);
    }

    /**
     * Kalkış Havaalanına(Departure) göre Arama - Havaalanından KALKAN Flightlar.
     *
     * @param searchType    Kalkis Havaalanı neye görene aranacak?
     *                      Isime göre(byname) - sadece isime göre arar,
     *                      sehire göre(bycity) - sadece sehire göre arar,
     *                      isim veya sehire göre(bynameorcity) - hem isim hem sehir icinde arar,

     * @param nameOrCity    Aranacak kelime
     * @return              Havaalanı/Havaalanlarından kalkan ucus modelleri
     * @throws CustomNotFound aranan degerde havaalanı veya kalkan ucus bulunamazsa
     */
    @GetMapping("/departure")
    @ApiOperation(value = "getFlightsByDeparture",notes = "This endpoint serves the flights by departure airports according to the search parameter sent.")
    public ResponseEntity<List<FlightResponseDTO>> getFlightsByDeparture(
            @ApiParam(value = "Search Parameter.",required = true) @RequestParam(required = true,name = "type") SearchType searchType,
            @ApiParam(value = "Search Value.",required = true) @RequestParam(required = true,name = "value") String nameOrCity) throws CustomNotFound {
       return new ResponseEntity<>(flightService.getFlightsByDeparture(searchType,nameOrCity),HttpStatus.OK);
    }

    /**Varis Havaalanına(Arrival) göre Arama - Havaalanına INEN Flightlar.
     *
     * @param searchType    Varis Havaalanı neye görene aranacak?
     *                      Isime göre(byname) - sadece isime göre arar,
     *                      sehire göre(bycity) - sadece sehire göre arar,
     *                      isim veya sehire göre(bynameorcity) - hem isim hem sehir icinde arar,
     * @param nameOrCity    Aranacak kelime
     * @return              Havaalanı/Havaalanlarından kalkan ucus modelleri
     * @throws CustomNotFound aranan degerde havaalanı veya inen ucus bulunamazsa
     */
    @GetMapping("/arrival")
    @ApiOperation(value = "getFlightsByArrival",notes = "This endpoint serves the flights by arrival airports according to the search parameter sent.")
    public ResponseEntity<List<FlightResponseDTO>> getFlightsByArrival(
            @ApiParam(value = "Search Parameter.",required = true) @RequestParam(required = true,name = "type") SearchType searchType,
            @ApiParam(value = "Search Value.",required = true) @RequestParam(required = true,name = "value") String nameOrCity) throws CustomNotFound {
        return new ResponseEntity<>(flightService.getFlightsByArrival(searchType,nameOrCity),HttpStatus.OK);
    }

    /**
     *Kalkış Havaalanına(Departure)  VE  Varis Havaalanına(Arrival) göre Arama - Departure'dan kalkip, Arrivalda inen flightlar.
     *
     * @param searchType   Varis ve kalkis Havaalanı neye görene aranacak?
     *                     Isime göre(byname) - sadece isime göre arar,
     *                     sehire göre(bycity) - sadece sehire göre arar,
     *                     isim veya sehire göre(bynameorcity) - hem isim hem sehir icinde arar,
     * @param departure    Kalkis havaalanı
     * @param arrival      Varis havaalani
     * @return             Havaalanı/Havaalanlarından kalkan ucus modelleri
     * @throws CustomNotFound Kalkıs ya da inis havaalanı veya aranan degerlerde ucus bulunamazsa
     */
    @GetMapping("/temp")
    @ApiOperation(value = "getFlightsByDepartureAndArrival",notes = "This endpoint serves the flights by arrival and departure airports according to the search parameter sent.")
    public ResponseEntity<FlightsResponseDTO> getFlightsByDepartureAndArrival(
            @ApiParam(value = "Search Parameter.",required = true) @RequestParam(required = true,name = "type") SearchType searchType,
            @ApiParam(value = "Search Value.",required = true) @RequestParam(required = true,name = "departure") String departure,
            @ApiParam(value = "Search Value.",required = true) @RequestParam(required = true,name = "arrival") String arrival) throws CustomNotFound {
        return new ResponseEntity<>(flightService.getFlightsByDepartureAndArrival(searchType,departure,arrival),HttpStatus.OK);
    }
}
