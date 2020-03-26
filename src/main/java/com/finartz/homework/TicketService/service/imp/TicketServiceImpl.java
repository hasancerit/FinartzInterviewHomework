package com.finartz.homework.TicketService.service.imp;

import com.finartz.homework.TicketService.domain.Ticket;
import com.finartz.homework.TicketService.dto.request.TicketRequestDTO;
import com.finartz.homework.TicketService.dto.response.TicketResponseDTO;
import com.finartz.homework.TicketService.repositories.FlightRepository;
import com.finartz.homework.TicketService.repositories.TicketRepository;
import com.finartz.homework.TicketService.repositories.UserRepository;
import com.finartz.homework.TicketService.service.TicketService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TicketResponseDTO saveTicket(TicketRequestDTO ticketDto) {
        Ticket ticket = modelMapper.map(ticketDto,Ticket.class);
        ticket.setFlight(flightRepository.getOne(ticketDto.getFlightId()));
        //ticket.setUser(userRepository.getOne(ticketDto.getUserId()));
        ticketRepository.save(ticket);
        return modelMapper.map(ticket,TicketResponseDTO.class);
    }

    @Override
    public TicketResponseDTO getTicket(String id) {
        return modelMapper.map(ticketRepository.getOne(id),TicketResponseDTO.class);
    }
}
