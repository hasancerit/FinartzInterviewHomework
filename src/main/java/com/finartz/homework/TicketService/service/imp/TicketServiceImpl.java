package com.finartz.homework.TicketService.service.imp;

import com.finartz.homework.TicketService.domain.Airport;
import com.finartz.homework.TicketService.domain.Flight;
import com.finartz.homework.TicketService.domain.Ticket;
import com.finartz.homework.TicketService.dto.request.TicketRequestDTO;
import com.finartz.homework.TicketService.dto.response.TicketResponseDTO;
import com.finartz.homework.TicketService.exception.exception.ApiException;
import com.finartz.homework.TicketService.repositories.FlightRepository;
import com.finartz.homework.TicketService.repositories.TicketRepository;
import com.finartz.homework.TicketService.service.TicketService;
import com.finartz.homework.TicketService.util.FlightClass;
import com.finartz.homework.TicketService.util.SeatStatus;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TicketResponseDTO saveTicket(TicketRequestDTO ticketDto) throws ApiException {
        Ticket ticket = modelMapper.map(ticketDto, Ticket.class);
        try {
            ticket.setFlight(flightRepository.findById(ticketDto.getFlightId()).get());
        } catch (NoSuchElementException ex) {
            throw new ApiException("Id Not Found", ticketDto.getClass(), "flightId", ticketDto.getFlightId());
        }
        Flight flight = ticket.getFlight();

        String seatNo = ticket.getNo();
        if (ticket.getFlightClass() == FlightClass.BUSINESS) {
            SeatStatus status = flight.getSeatsBusiness().get(seatNo);
            if(Integer.parseInt(seatNo) > flight.getSeatsBusiness().size()) //Sinir asildi ise
                throw new ApiException("Business capacity exceeded", ticketDto.getClass(), "no", ticketDto.getNo());
            if (status != SeatStatus.empty)  //Alınan Koltuk boş değil ise
                throw new ApiException("Seat is already taken.", ticketDto.getClass(), "no", ticketDto.getNo());
            flight.getSeatsBusiness().replace(seatNo, SeatStatus.taken);
        } else if (ticket.getFlightClass() == FlightClass.ECONOMI) {
            SeatStatus status = flight.getSeatsEconomic().get(seatNo);
            if(Integer.parseInt(seatNo) > flight.getSeatsEconomic().size()) //Ko
                throw new ApiException("Business capacity exceeded", ticketDto.getClass(), "no", ticketDto.getNo());
            if (status != SeatStatus.empty) //Alınan Koltuk boş değil ise
                throw new ApiException("Seat is already taken.", ticketDto.getClass(), "no", ticketDto.getNo());
            flight.getSeatsEconomic().replace(seatNo, SeatStatus.taken);
        }

        flightRepository.save(flight);
        ticketRepository.save(ticket);

        return modelMapper.map(ticket, TicketResponseDTO.class);
    }

    @Override
    public TicketResponseDTO getTicket(String id) {
        try{
            return modelMapper.map(ticketRepository.findById(id).get(), TicketResponseDTO.class);
        }catch (NoSuchElementException ex){
            return null;
        }
    }

    @Override
    public List<TicketResponseDTO> getAll() {
        List<TicketResponseDTO> ticketResponseDTOList = new ArrayList<>();
        ticketRepository.findAll().stream().forEach(ticket -> {
            ticketResponseDTOList.add(modelMapper.map(ticket, TicketResponseDTO.class));
        });
        return null;
    }

    @Override
    public TicketResponseDTO getTickeyByTicketNo(String ticketNo) {
        return modelMapper.map(ticketRepository.findByTicketNo(ticketNo),TicketResponseDTO.class);
    }

    @Override
    public void deleteTicket(String id) throws ApiException {
        Ticket ticket;
        try{
            ticket = ticketRepository.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new ApiException("ticketId Not Found",id.getClass(),"ticketId",id);
        }
        ticketRepository.delete(ticket);
    }
}
