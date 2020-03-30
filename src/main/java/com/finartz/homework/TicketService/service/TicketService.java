package com.finartz.homework.TicketService.service;

import com.finartz.homework.TicketService.dto.request.TicketRequestDTO;
import com.finartz.homework.TicketService.dto.response.TicketResponseDTO;
import com.finartz.homework.TicketService.exception.exception.ApiException;

import java.util.List;

public interface TicketService {
    TicketResponseDTO saveTicket(TicketRequestDTO ticketDto) throws ApiException;

    TicketResponseDTO getTicket(String id);

    List<TicketResponseDTO> getAll();
}
