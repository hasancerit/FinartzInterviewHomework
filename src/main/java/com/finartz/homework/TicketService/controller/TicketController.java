package com.finartz.homework.TicketService.controller;

import com.finartz.homework.TicketService.config.SwaggerConfig;
import com.finartz.homework.TicketService.dto.request.TicketRequestDTO;
import com.finartz.homework.TicketService.dto.response.TicketResponseDTO;
import com.finartz.homework.TicketService.exception.exception.CustomAlreadyTaken;
import com.finartz.homework.TicketService.exception.exception.CustomNotFound;
import com.finartz.homework.TicketService.service.TicketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequiredArgsConstructor
@RestController
@Api(tags = {SwaggerConfig.TAG_4})
@RequestMapping("/ticket")
public class TicketController {
    private final TicketService ticketService;

    /**
     * Ekleme
     *
     * @param ticketRequestDto  Kaydedilecek Ticket'ın modeli
     * @return                  Kaydedilen Ticket'ın modeli
     * @throws CustomAlreadyTaken   Business,Economi dolu ise,(Validation ile)
     *                              Kapasite asildi ise,      (Validation ile)
     *                              Koltuk daha once alindi ise
     * @throws CustomNotFound       flightId bulunamazsa
     */
    @PostMapping("/add")
    @ApiOperation(value = "saveTicket",notes = "This endpoint saves the successfully sent ticket.")
    public ResponseEntity<TicketResponseDTO> saveTicket(
            @ApiParam(value = "Ticket Model to will be saved",required = true) @Valid @RequestBody TicketRequestDTO ticketRequestDto) throws CustomNotFound,CustomAlreadyTaken {
        return new ResponseEntity<>(ticketService.saveTicket(ticketRequestDto), HttpStatus.OK);
    }

    /**
     * Güncelleme
     *
     * @param id            Guncellenecek Ticket id'si
     * @param ticketDto     Guncellenecek Ticket'ın yeni alanları
     * @return              Guncellenen Ticket'ın modeli
     * @throws CustomAlreadyTaken   Business,Economi dolu ise,(Validation ile)
     *                              Kapasite asildi ise,      (Validation ile)
     *                              Koltuk daha once alindi ise
     * @throws CustomNotFound       ticketId bulunamazsa
     *                              flightId bulunamazsa
     */
    @PostMapping("/update/{id}")
    @ApiOperation(value = "updateTicket",notes = "This endpoint updates the successfully sent .")
    public ResponseEntity<TicketResponseDTO> updateTicket(
            @ApiParam(value = "Id of the ticket to be updated.",required = true) @PathVariable String id,
            @ApiParam(value = "Ticket Model to will be updated.",required = true)@Valid @RequestBody TicketRequestDTO ticketDto) throws CustomAlreadyTaken, CustomNotFound {
        return new ResponseEntity<>(ticketService.updateTicket(id,ticketDto),HttpStatus.OK);
    }

    /**
     * Silme
     *
     * @param id                Silinecek Ticket id'si
     * @throws CustomNotFound   ticketId bulunamazsa
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "deleteTicket",notes = "This endpoint deletes ticket of the successfully sent Id.")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteTicket(
            @ApiParam(value = "Id of the ticket to be deleted.",required = true) @PathVariable String id) throws CustomNotFound {
        ticketService.deleteTicket(id);
    }

    /**
     * Hepsini cek
     *
     * @return Veritabanındaki tüm Ticket'lar.
     */
    @GetMapping("/all")
    @ApiOperation(value = "getAll",notes = "This endpoint serves all tickets.")
    public ResponseEntity getAll(){
        return new ResponseEntity<>(ticketService.getAll(), HttpStatus.OK);
    }

    /**
     * Id İle Arama
     *
     * @param id                Alinmak istenen Ticket id'si
     * @return                  Istenen Tikcet modeli
     * @throws CustomNotFound   ticketId bulunamazsa
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "getTicket",notes = "This endpoint serves ticket of the successfully sent Id.")
    public ResponseEntity<TicketResponseDTO> getTicket(
            @ApiParam(value = "Id of the ticket to be served.",required = true) @PathVariable String id) throws CustomNotFound {
        return new ResponseEntity<>(ticketService.getTicket(id),HttpStatus.OK);
    }

    /**
     * TicketNo'ya göre bul
     *
     * @param ticketNo          Alinmak istenen Ticket id'si
     * @return                  Istenen Tikcet modeli
     * @throws CustomNotFound   ticketId bulunamazsa
     */
    @GetMapping("/pnr")
    @ApiOperation(value = "getTicket",notes = "This endpoint serves ticket of the successfully sent ticket id.")
    public ResponseEntity<TicketResponseDTO> getTicketByTicketNo(
            @ApiParam(value = "Ticket No of the ticket to be served.",required = true) @RequestParam(required = true,name = "pnr") String ticketNo) throws CustomNotFound {
        return new ResponseEntity<>(ticketService.getTickeyByTicketNo(ticketNo),HttpStatus.OK);
    }
}
