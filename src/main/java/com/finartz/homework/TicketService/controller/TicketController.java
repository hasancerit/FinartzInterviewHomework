package com.finartz.homework.TicketService.controller;

import com.finartz.homework.TicketService.dto.request.TicketRequestDTO;
import com.finartz.homework.TicketService.dto.response.TicketResponseDTO;
import com.finartz.homework.TicketService.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ticket")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @PostMapping("/add")
    public ResponseEntity<TicketResponseDTO> saveTicket(@RequestBody TicketRequestDTO airportDto){
        return new ResponseEntity<>(ticketService.saveTicket(airportDto), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> saveTicket(@PathVariable String id){
        return new ResponseEntity<>(ticketService.getTicket(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity getAll(){
        return new ResponseEntity<>(ticketService.getAll(), HttpStatus.OK);
    }

    /**Sil Ve Güncelle**/
}
