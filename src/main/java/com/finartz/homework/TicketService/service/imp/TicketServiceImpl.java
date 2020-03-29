package com.finartz.homework.TicketService.service.imp;

import com.finartz.homework.TicketService.domain.Flight;
import com.finartz.homework.TicketService.domain.Ticket;
import com.finartz.homework.TicketService.dto.request.TicketRequestDTO;
import com.finartz.homework.TicketService.dto.response.TicketResponseDTO;
import com.finartz.homework.TicketService.exception.exception.TakenSeatException;
import com.finartz.homework.TicketService.repositories.FlightRepository;
import com.finartz.homework.TicketService.repositories.TicketRepository;
import com.finartz.homework.TicketService.service.TicketService;
import com.finartz.homework.TicketService.util.FlightClass;
import com.finartz.homework.TicketService.util.SeatStatus;
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
    private ModelMapper modelMapper;

    @Override
    public TicketResponseDTO saveTicket(TicketRequestDTO ticketDto) {
        Ticket ticket = modelMapper.map(ticketDto, Ticket.class);

        ticket.setFlight(flightRepository.getOne(ticketDto.getFlightId())); //Olmayan Flight hatası
        Flight flight = ticket.getFlight();

        String seatNo = ticket.getNo();
        if (ticket.getFlightClass() == FlightClass.BUSINESS) {
            SeatStatus status = flight.getSeatsBusiness().get(seatNo);
            if(status != SeatStatus.empty){} //Alınan Koltuk boş değil ise
                //Hata Fırlat
            flight.getSeatsBusiness().replace(seatNo,SeatStatus.taken);
        } else if (ticket.getFlightClass() == FlightClass.ECONOMİ) {
            SeatStatus status = flight.getSeatsEconomic().get(seatNo);
            if(status != SeatStatus.empty){} //Alınan Koltuk boş değil ise
                //Hata Fırlat
             flight.getSeatsEconomic().replace(seatNo,SeatStatus.taken);
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
