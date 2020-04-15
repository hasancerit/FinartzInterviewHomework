package com.finartz.homework.TicketService.service;

import com.finartz.homework.TicketService.dto.request.TicketRequestDTO;
import com.finartz.homework.TicketService.dto.response.TicketResponseDTO;
import com.finartz.homework.TicketService.exception.exception.CustomAlreadyTaken;
import com.finartz.homework.TicketService.exception.exception.CustomNotFound;

import java.util.List;

public interface TicketService {
    TicketResponseDTO saveTicket(TicketRequestDTO ticketReqDto) throws CustomAlreadyTaken, CustomNotFound;

    TicketResponseDTO updateTicket(String id, TicketRequestDTO ticketReqDto) throws CustomAlreadyTaken, CustomNotFound;

    void deleteTicket(String id) throws CustomNotFound;

    List<TicketResponseDTO> getAll();

    TicketResponseDTO getTicket(String id) throws CustomNotFound;

    TicketResponseDTO getTickeyByPnr(String pnr) throws CustomNotFound;
}
