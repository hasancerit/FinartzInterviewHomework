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

@RequiredArgsConstructor
@RestController
@Api(tags = {SwaggerConfig.TAG_4})
@RequestMapping("/ticket")
public class TicketController {
    private final TicketService ticketService;

    /*Ekleme*/
    @PostMapping("/add")
    @ApiOperation(value = "saveTicket",notes = "This endpoint saves the successfully sent ticket.")
    public ResponseEntity<TicketResponseDTO> saveTicket(
            @ApiParam(value = "Ticket Model to will be saved",required = true) @Valid @RequestBody TicketRequestDTO ticketRequestDto) throws ApiException {
        return new ResponseEntity<>(ticketService.saveTicket(ticketRequestDto), HttpStatus.OK);
    }

    /*Güncelleme*/
    @PostMapping("/update/{id}")
    @ApiOperation(value = "updateTicket",notes = "This endpoint updates the successfully sent .")
    public ResponseEntity<TicketResponseDTO> updateTicket(
            @ApiParam(value = "Id of the ticket to be updated.",required = true) @PathVariable String id,
            @ApiParam(value = "Ticket Model to will be updated.",required = true) @RequestBody TicketRequestDTO ticketDto) throws ApiException {
        return new ResponseEntity<>(ticketService.updateTicket(id,ticketDto),HttpStatus.OK);
    }

    /*Silme*/
    @DeleteMapping("/{id}")
    @ApiOperation(value = "deleteTicket",notes = "This endpoint deletes ticket of the successfully sent Id.")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteTicket(
            @ApiParam(value = "Id of the ticket to be deleted.",required = true) @PathVariable String id) throws ApiException {
        ticketService.deleteTicket(id);
    }


    /*Hepsini bul*/
    @GetMapping("/all")
    @ApiOperation(value = "getAll",notes = "This endpoint serves all tickets.")
    public ResponseEntity getAll(){
        return new ResponseEntity<>(ticketService.getAll(), HttpStatus.OK);
    }

    /*İd'e göre bul*/
    @GetMapping("/{id}")
    @ApiOperation(value = "getTicket",notes = "This endpoint serves ticket of the successfully sent Id.")
    public ResponseEntity<TicketResponseDTO> getTicket(
            @ApiParam(value = "Id of the ticket to be served.",required = true) @PathVariable String id){
        return new ResponseEntity<>(ticketService.getTicket(id), HttpStatus.OK);
    }

    /*TicketNo'ya göre bul*/
    @GetMapping("/pnr")
    @ApiOperation(value = "getTicket",notes = "This endpoint serves ticket of the successfully sent ticket id.")
    public ResponseEntity<TicketResponseDTO> getTicketByTicketNo(
            @ApiParam(value = "Ticket No of the ticket to be served.",required = true) @RequestParam(required = true,name = "pnr") String ticketNo){
        return new ResponseEntity<>(ticketService.getTickeyByTicketNo(ticketNo), HttpStatus.OK);
    }
}
