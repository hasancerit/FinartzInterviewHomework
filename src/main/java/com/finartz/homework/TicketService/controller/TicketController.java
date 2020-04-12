package com.finartz.homework.TicketService.controller;

import com.finartz.homework.TicketService.config.SwaggerConfig;
import com.finartz.homework.TicketService.dto.request.TicketRequestDTO;
import com.finartz.homework.TicketService.dto.response.TicketResponseDTO;
import com.finartz.homework.TicketService.exception.exception.ApiException;
import com.finartz.homework.TicketService.service.TicketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.finartz.homework.TicketService.util.ResponseHandler.createResponse;

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
     * @throws ApiException     Ticket Name zaten varsa
     */
    @PostMapping("/add")
    @ApiOperation(value = "saveTicket",notes = "This endpoint saves the successfully sent ticket.")
    public ResponseEntity<TicketResponseDTO> saveTicket(
            @ApiParam(value = "Ticket Model to will be saved",required = true) @Valid @RequestBody TicketRequestDTO ticketRequestDto) throws ApiException {
        return new ResponseEntity<>(ticketService.saveTicket(ticketRequestDto), HttpStatus.OK);
    }

    /**
     * Güncelleme
     *
     * @param id            Guncellenecek Ticket id'si
     * @param ticketDto     Guncellenecek Ticket'ın yeni alanları
     * @return              Guncellenen Ticket'ın modeli
     * @throws ApiException Ticket id bulunamazsa
     */
    @PostMapping("/update/{id}")
    @ApiOperation(value = "updateTicket",notes = "This endpoint updates the successfully sent .")
    public ResponseEntity<TicketResponseDTO> updateTicket(
            @ApiParam(value = "Id of the ticket to be updated.",required = true) @PathVariable String id,
            @ApiParam(value = "Ticket Model to will be updated.",required = true)@Valid @RequestBody TicketRequestDTO ticketDto) throws ApiException {
        return new ResponseEntity<>(ticketService.updateTicket(id,ticketDto),HttpStatus.OK);
    }

    /**
     * Silme
     *
     * @param id            Silinecek Ticket id'si
     * @throws ApiException Airline Ticket bulunamazsa
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "deleteTicket",notes = "This endpoint deletes ticket of the successfully sent Id.")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteTicket(
            @ApiParam(value = "Id of the ticket to be deleted.",required = true) @PathVariable String id) throws ApiException {
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
     * @param id    Alinmak istenen Ticket id'si
     * @return      Istenen Tikcet modeli - Id bulunmaz ise null döner.
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "getTicket",notes = "This endpoint serves ticket of the successfully sent Id.")
    public ResponseEntity<?> getTicket(
            @ApiParam(value = "Id of the ticket to be served.",required = true) @PathVariable String id){
        return createResponse(ticketService.getTicket(id));
    }

    /**
     * TicketNo'ya göre bul
     *
     * @param ticketNo   Alinmak istenen Ticket id'si
     * @return           Istenen Tikcet modeli - Id bulunmaz ise null döner.
     */
    @GetMapping("/pnr")
    @ApiOperation(value = "getTicket",notes = "This endpoint serves ticket of the successfully sent ticket id.")
    public ResponseEntity<?> getTicketByTicketNo(
            @ApiParam(value = "Ticket No of the ticket to be served.",required = true) @RequestParam(required = true,name = "pnr") String ticketNo){
        return createResponse(ticketService.getTickeyByTicketNo(ticketNo));
    }
}
