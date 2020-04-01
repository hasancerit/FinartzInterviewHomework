package com.finartz.homework.TicketService.controller;

import com.finartz.homework.TicketService.dto.request.TicketRequestDTO;
import com.finartz.homework.TicketService.dto.response.TicketResponseDTO;
import com.finartz.homework.TicketService.exception.exception.ApiException;
import com.finartz.homework.TicketService.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/ticket")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @PostMapping("/add")
    public ResponseEntity<TicketResponseDTO> saveTicket(@Valid @RequestBody TicketRequestDTO airportDto) throws ApiException {
        return new ResponseEntity<>(ticketService.saveTicket(airportDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteTicket(@PathVariable String id) throws ApiException {
        ticketService.deleteTicket(id);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> getTicket(@PathVariable String id){
        return new ResponseEntity<>(ticketService.getTicket(id), HttpStatus.OK);
    }

    @GetMapping("/pnr")
    public ResponseEntity<TicketResponseDTO> getTicketByTicketNo(@RequestParam(required = true,name = "pnr") String ticketNo){
        return new ResponseEntity<>(ticketService.getTickeyByTicketNo(ticketNo), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity getAll(){
        return new ResponseEntity<>(ticketService.getAll(), HttpStatus.OK);
    }

    /**Sil Ve Güncelle**/
}
