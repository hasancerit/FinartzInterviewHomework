package com.finartz.homework.TicketService.service.imp;

import com.finartz.homework.TicketService.domain.Flight;
import com.finartz.homework.TicketService.domain.Ticket;
import com.finartz.homework.TicketService.dto.request.TicketRequestDTO;
import com.finartz.homework.TicketService.dto.response.TicketResponseDTO;
import com.finartz.homework.TicketService.repositories.FlightRepository;
import com.finartz.homework.TicketService.repositories.TicketRepository;
import com.finartz.homework.TicketService.repositories.UserRepository;
import com.finartz.homework.TicketService.service.TicketService;
import com.finartz.homework.TicketService.util.FlightClass;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Ticket ticket = modelMapper.map(ticketDto, Ticket.class);

        /*Ticket To Ticket DTO*/
        ticket.setFlight(flightRepository.getOne(ticketDto.getFlightId())); //Olmayan Flight hatası
        //ticket.setUser(userRepository.getOne(ticketDto.getUserId()));

        /*Uçuştaki dolu yerleri güncelle*/
        Flight flight = ticket.getFlight();
        if (ticket.getFlightClass() == FlightClass.BUSINESS) {
            flight.setTakenSeatsBusiness(
                    Stream.concat(flight.getTakenSeatsBusiness().stream(), Stream.of(ticket.getNo()))
                            .collect(Collectors.toList()));
        } else if (ticket.getFlightClass() == FlightClass.ECONOMİ) {
            flight.setTakenSeatsEconomi(
                    Stream.concat(flight.getTakenSeatsEconomi().stream(), Stream.of(ticket.getNo()))
                            .collect(Collectors.toList()));
        }
        flightRepository.save(flight);

        /*Bileti Al*/
        ticketRepository.save(ticket);

        return modelMapper.map(ticket, TicketResponseDTO.class);
    }

    @Override
    public TicketResponseDTO getTicket(String id) {
        return modelMapper.map(ticketRepository.getOne(id), TicketResponseDTO.class);
    }

    @Override
    public List<TicketResponseDTO> getAll() {
        List<TicketResponseDTO> ticketResponseDTOList = new ArrayList<>();
        ticketRepository.findAll().stream().forEach( ticket -> {
            ticketResponseDTOList.add(modelMapper.map(ticket,TicketResponseDTO.class));
        });
        return null;
    }
}
