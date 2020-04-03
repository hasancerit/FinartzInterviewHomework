package com.finartz.homework.TicketService.service.imp;

import com.finartz.homework.TicketService.domain.Flight;
import com.finartz.homework.TicketService.domain.Ticket;
import com.finartz.homework.TicketService.dto.request.TicketRequestDTO;
import com.finartz.homework.TicketService.dto.response.TicketResponseDTO;
import com.finartz.homework.TicketService.exception.exception.ApiException;
import com.finartz.homework.TicketService.repositories.FlightRepository;
import com.finartz.homework.TicketService.repositories.TicketRepository;
import com.finartz.homework.TicketService.service.TicketService;
import com.finartz.homework.TicketService.domain.Seat;
import com.finartz.homework.TicketService.util.FlightClass;
import com.finartz.homework.TicketService.util.SeatStatus;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /*Ekleme*/
    @Override
    public TicketResponseDTO saveTicket(TicketRequestDTO ticketDto) throws ApiException {
        Ticket ticket = modelMapper.map(ticketDto, Ticket.class);
        return modelMapper.map(handleSaveTicket(ticket,ticketDto), TicketResponseDTO.class);
    }

    /*Güncelleme*/
    @Override
    public TicketResponseDTO updateTicket(String id, TicketRequestDTO ticketDto) throws ApiException {
        Ticket ticket = null;
        try {
            ticket = ticketRepository.findById(id).get();
        } catch(NoSuchElementException ex) {
            throw new ApiException("ticketId Not Found",id.getClass(),"ticketId",id);
        }

        //Eski koltugu kaldirmak icin.
        String oldFlightId = ticket.getFlight().getId();
        FlightClass oldClass = ticket.getFlightClass();
        String oldNo = ticket.getNo();

        //Yeni ticketı güncelle
        ticket.setPassanger(ticketDto.getPassanger());
        ticket.setFlightClass(ticketDto.getFlightClass());
        ticket.setNo(ticketDto.getNo());
        ticket.setFlight(flightRepository.getOne(ticketDto.getFlightId()));

        //Eski koltugu kaldir.
        Flight oldFlight = flightRepository.getOne(oldFlightId);
        if(oldClass == FlightClass.BUSINESS){
            oldFlight.getSeatsBusiness().replace(oldNo,new Seat( SeatStatus.empty,null));
        }else{
            oldFlight.getSeatsEconomic().replace(oldNo,new Seat( SeatStatus.empty,null));
        }

        TicketResponseDTO responseDTO = modelMapper.map(handleSaveTicket(ticket,ticketDto),TicketResponseDTO.class);

        flightRepository.save(oldFlight);
        return responseDTO;
    }

    private Ticket handleSaveTicket(Ticket ticket,TicketRequestDTO ticketDto) throws ApiException {
        try {
            ticket.setFlight(flightRepository.findById(ticketDto.getFlightId()).get());
        } catch (NoSuchElementException ex) {
            throw new ApiException("Id Not Found", ticketDto.getClass(), "flightId", ticketDto.getFlightId());
        }
        Flight flight = ticket.getFlight();

        String seatNo = ticket.getNo();
        if (ticket.getFlightClass() == FlightClass.BUSINESS) {
            if(Integer.parseInt(seatNo) > flight.getSeatsBusiness().size()){//Sinir asildi ise
                throw new ApiException("Business capacity exceeded", ticketDto.getClass(), "no", ticketDto.getNo());
            }
            //isFull
            SeatStatus status = flight.getSeatsBusiness().get(seatNo).getSeatStatus();
            if (status != SeatStatus.empty){                                //Alınan Koltuk boş değil ise
                throw new ApiException("Seat is already taken.", ticketDto.getClass(), "no", ticketDto.getNo());
            }
            flight.getSeatsBusiness().replace(seatNo,new Seat(SeatStatus.taken,ticket));
            //isFull
        }
        else if (ticket.getFlightClass() == FlightClass.ECONOMI) {
            if(Integer.parseInt(seatNo) > flight.getSeatsEconomic().size()){//Sinir asildi ise
                throw new ApiException("Business capacity exceeded", ticketDto.getClass(), "no", ticketDto.getNo());
            }
            //isFull
            SeatStatus status = flight.getSeatsEconomic().get(seatNo).getSeatStatus();
            if (status != SeatStatus.empty) {                               //Alınan Koltuk boş değil ise
                throw new ApiException("Seat is already taken.", ticketDto.getClass(), "no", ticketDto.getNo());
            }
            //isFull
            flight.getSeatsEconomic().replace(seatNo,new Seat(SeatStatus.taken,ticket));
        }

        ticketRepository.save(ticket);
        flightRepository.save(flight);
        return ticket;
    }

    /*Silme*/
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


    /*Hepsini Bul*/
    @Override
    public List<TicketResponseDTO> getAll() {
        List<TicketResponseDTO> ticketResponseDTOList = new ArrayList<>();
        ticketRepository.findAll().stream().forEach(ticket -> {
            ticketResponseDTOList.add(modelMapper.map(ticket, TicketResponseDTO.class));
        });
        return null;
    }

    /*Id'ye göre Bul*/
    @Override
    public TicketResponseDTO getTicket(String id) {
        try{
            return modelMapper.map(ticketRepository.findById(id).get(), TicketResponseDTO.class);
        }catch (NoSuchElementException ex){
            return null;
        }
    }

    /*TicketNo'ya göre bul*/
    @Override
    public TicketResponseDTO getTickeyByTicketNo(String ticketNo) {
        return modelMapper.map(ticketRepository.findByTicketNo(ticketNo),TicketResponseDTO.class);
    }


}
