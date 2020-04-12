package com.finartz.homework.TicketService.service;

import com.finartz.homework.TicketService.dto.request.TicketRequestDTO;
import com.finartz.homework.TicketService.dto.response.TicketResponseDTO;
import com.finartz.homework.TicketService.exception.exception.CustomAlreadyTaken;
import com.finartz.homework.TicketService.exception.exception.CustomNotFound;

import java.util.List;

public interface TicketService {
    TicketResponseDTO saveTicket(TicketRequestDTO ticketDto) throws CustomAlreadyTaken, CustomNotFound;

    TicketResponseDTO updateTicket(String id, TicketRequestDTO ticketDto) throws CustomAlreadyTaken, CustomNotFound;

    void deleteTicket(String id) throws CustomAlreadyTaken, CustomNotFound;

    List<TicketResponseDTO> getAll();

    TicketResponseDTO getTicket(String id) throws CustomNotFound;

    TicketResponseDTO getTickeyByTicketNo(String ticketNo) throws CustomNotFound;
}
