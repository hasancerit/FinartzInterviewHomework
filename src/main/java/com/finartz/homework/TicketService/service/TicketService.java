package com.finartz.homework.TicketService.service;

import com.finartz.homework.TicketService.domain.Ticket;
import com.finartz.homework.TicketService.dto.request.TicketRequestDTO;
import com.finartz.homework.TicketService.dto.response.TicketResponseDTO;

import java.util.List;

public interface TicketService {
    TicketResponseDTO saveTicket(TicketRequestDTO ticketDto);

    TicketResponseDTO getTicket(String id);

    List<TicketResponseDTO> getAll();
}
