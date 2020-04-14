package com.finartz.homework.TicketService.service;

import com.finartz.homework.TicketService.domain.Airport;
import com.finartz.homework.TicketService.domain.Flight;
import com.finartz.homework.TicketService.domain.Ticket;
import com.finartz.homework.TicketService.domain.embeddable.Passanger;
import com.finartz.homework.TicketService.domain.embeddable.Seat;
import com.finartz.homework.TicketService.dto.request.TicketRequestDTO;
import com.finartz.homework.TicketService.dto.response.TicketResponseDTO;
import com.finartz.homework.TicketService.exception.exception.CustomAlreadyTaken;
import com.finartz.homework.TicketService.exception.exception.CustomNotFound;
import com.finartz.homework.TicketService.repositories.FlightRepository;
import com.finartz.homework.TicketService.repositories.TicketRepository;
import com.finartz.homework.TicketService.service.imp.TicketServiceImpl;
import com.finartz.homework.TicketService.util.FlightClass;
import com.finartz.homework.TicketService.util.Gender;
import com.finartz.homework.TicketService.util.SeatStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private FlightRepository flightRepository;
    @Mock
    private ModelMapper modelMapper;

    private TicketService ticketService;

    //Testlerde, TicketRequestDto'da kullanılacak Passanger nesnesi
    private Passanger passanger;
    //Testlerde, TicketResposeDto'dan donecek Flight nesnesi.
    private Flight flight;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ticketService = new TicketServiceImpl(ticketRepository,flightRepository,modelMapper);

        passanger = createPassanger();
        flight = createFlight();
     }

    @Test
    void saveTicket() throws CustomAlreadyTaken, CustomNotFound {
        //Kaydedilecek Ticket
        TicketRequestDTO ticketRequestDTO = new TicketRequestDTO(flight.getId(), FlightClass.BUSINESS,passanger,"23");

        //Vt'ye kaydedilip donecek ticket
        Ticket ticket = modelMapper.map(ticketRequestDTO,Ticket.class);
        ticket.setFlight(flight);
        ticket.setId("3");

        given(ticketRepository.save(any())).willReturn(ticket);
        given(flightRepository.findById(flight.getId())).willReturn(Optional.of(flight));

        TicketResponseDTO ticketResponseDTO = ticketService.saveTicket(ticketRequestDTO);

        assertEquals(ticketResponseDTO.getId(),"3");
        assertEquals(ticketResponseDTO.getNo(),"23");
        assertEquals(ticketResponseDTO.getFlightClass(),FlightClass.BUSINESS);

        assertEquals(ticketResponseDTO.getFlight().getId(),"1");
        assertEquals(ticketResponseDTO.getFlight().getDeparture().getName(),"Sabiha Gökçen");
        assertEquals(ticketResponseDTO.getFlight().getSeatsBusiness().get("23").getTicket().getId(), "3");
        assertEquals(ticketResponseDTO.getFlight().getSeatsBusiness().get("23").getSeatStatus(), SeatStatus.taken);

        assertEquals(ticketResponseDTO.getPassanger().getFullName(),"Hasan Cerit");
    }

    @Test
    void saveTicketFlightNotFound() throws CustomAlreadyTaken, CustomNotFound {
        //Kaydedilecek Ticket
        TicketRequestDTO ticketRequestDTO = new TicketRequestDTO(flight.getId(), FlightClass.BUSINESS,passanger,"23");

        CustomNotFound e = assertThrows(CustomNotFound.class,() -> {
            ticketService.saveTicket(ticketRequestDTO);
        });

        assertEquals(e.getField(),"flightId");
        assertEquals(e.getValue(),ticketRequestDTO.getFlightId());

    }

    @Test
    void saveTicketToFullFlight() throws CustomAlreadyTaken, CustomNotFound {
        flight.setFullBusiness(true);
        //Kaydedilecek Ticket
        TicketRequestDTO ticketRequestDTO = new TicketRequestDTO(flight.getId(), FlightClass.BUSINESS,passanger,"23");

        given(flightRepository.findById(flight.getId())).willReturn(Optional.of(flight));

        CustomAlreadyTaken e = assertThrows(CustomAlreadyTaken.class,() -> {
            ticketService.saveTicket(ticketRequestDTO);
        });

        assertEquals(e.getField(),"capasity");
        assertEquals(e.getMessage(),FlightClass.BUSINESS+ " İs Full");
        assertEquals(e.getValue(),"Capasity:"+flight.getCapasityBusiness());
    }

    @Test
    void saveTicketCapasityExceed() throws CustomAlreadyTaken, CustomNotFound {
        //Kaydedilecek Ticket
        TicketRequestDTO ticketRequestDTO = new TicketRequestDTO(flight.getId(),
                FlightClass.BUSINESS,
                passanger,
                ""+flight.getCapasityBusiness()+1);

        given(flightRepository.findById(flight.getId())).willReturn(Optional.of(flight));

        CustomAlreadyTaken e = assertThrows(CustomAlreadyTaken.class,() -> {
            ticketService.saveTicket(ticketRequestDTO);
        });

        assertEquals(e.getField(),"no");
        assertEquals(e.getMessage(),FlightClass.BUSINESS+ " capacity exceeded");
        assertEquals(e.getValue(),ticketRequestDTO.getNo());
    }

    @Test
    void saveTicketTakenSeat() throws CustomAlreadyTaken, CustomNotFound {
        flight.getSeatsByFlightClass(FlightClass.BUSINESS).replace("23",new Seat(SeatStatus.taken,null));
        //Kaydedilecek Ticket
        TicketRequestDTO ticketRequestDTO = new TicketRequestDTO(flight.getId(), FlightClass.BUSINESS, passanger, "23");

        given(flightRepository.findById(flight.getId())).willReturn(Optional.of(flight));

        CustomAlreadyTaken e = assertThrows(CustomAlreadyTaken.class,() -> {
            ticketService.saveTicket(ticketRequestDTO);
        });

        assertEquals(e.getField(),"no");
        assertEquals(e.getMessage(),"Seat is already taken.");
        assertEquals(e.getValue(),ticketRequestDTO.getNo());
    }

    @Test
    void updateTicket() throws CustomAlreadyTaken, CustomNotFound {
        String id = "3"; //Guncellenecek ticket id

        //Eski Ticket
        Ticket oldTicket = new Ticket(id,"2b56u7",passanger,flight,FlightClass.BUSINESS,"23");
        given(ticketRepository.findById(id)).willReturn(Optional.of(oldTicket));

        //Guncellenecek Ticket
        passanger.setFullName("Batuhan Cerit");
        TicketRequestDTO ticketRequestDTO = new TicketRequestDTO(flight.getId(), FlightClass.ECONOMI,passanger,"24");

        //Vt'ye kaydedilip donecek ticket
        Ticket ticket = modelMapper.map(ticketRequestDTO,Ticket.class);
        ticket.setFlight(flight);
        ticket.setId(id);

        given(flightRepository.findById(any())).willReturn(Optional.ofNullable(flight));
        given(ticketRepository.save(any())).willReturn(ticket);

        TicketResponseDTO ticketResponseDTO = ticketService.updateTicket(id,ticketRequestDTO);

        assertEquals(ticketResponseDTO.getId(),"3");
        assertEquals(ticketResponseDTO.getNo(),"24");
        assertEquals(ticketResponseDTO.getFlightClass(),FlightClass.ECONOMI);

        assertEquals(ticketResponseDTO.getFlight().getId(),"1");
        assertEquals(ticketResponseDTO.getFlight().getDeparture().getName(),"Sabiha Gökçen");
        assertEquals(ticketResponseDTO.getFlight().getSeatsEconomic().get("24").getTicket().getId(), "3");
        assertEquals(ticketResponseDTO.getFlight().getSeatsEconomic().get("24").getSeatStatus(), SeatStatus.taken);

        assertEquals(ticketResponseDTO.getPassanger().getFullName(),"Batuhan Cerit");
    }

    @Test
    void updateTicketTicketNotFound() throws CustomAlreadyTaken, CustomNotFound {
        String id = "3"; //Guncellenecek ticket id

        //Guncellenecek Ticket
        TicketRequestDTO ticketRequestDTO = new TicketRequestDTO(flight.getId(), FlightClass.ECONOMI,passanger,"24");

        CustomNotFound e = assertThrows(CustomNotFound.class,() -> {
            ticketService.updateTicket(id,ticketRequestDTO);
        });

        assertEquals(e.getValue(),id);
        assertEquals(e.getField(),"ticketId");
    }

    @Test
    void deleteTicket() {
    }

    @Test
    void getAll() {
        List<Ticket> tickets = Arrays.asList(
                new Ticket("1","23r5t7",passanger,flight,FlightClass.BUSINESS,"21"),
                new Ticket("2","25x3h8",passanger,flight,FlightClass.ECONOMI,"24")
        );

        given(ticketRepository.findAll()).willReturn(tickets);

        List<TicketResponseDTO> responseDTOS = ticketService.getAll();

        assertEquals(responseDTOS.get(0).getId(),"1");
        assertEquals(responseDTOS.get(0).getFlightClass(),FlightClass.BUSINESS);
        assertEquals(responseDTOS.get(0).getNo(),"21");
        assertEquals(responseDTOS.get(1).getId(),"2");
        assertEquals(responseDTOS.get(1).getFlightClass(),FlightClass.ECONOMI);
        assertEquals(responseDTOS.get(1).getNo(),"24");

    }

    @Test
    void getTicket() throws CustomNotFound {
        String id = "1";
        Ticket ticket = new Ticket("1","23r5t7",passanger,flight,FlightClass.BUSINESS,"21");
        given(ticketRepository.findById(id)).willReturn(Optional.of(ticket));

        TicketResponseDTO ticketResponseDTO = ticketService.getTicket(id);

        assertEquals(ticketResponseDTO.getId(),"1");
        assertEquals(ticketResponseDTO.getNo(),"21");
        assertEquals(ticketResponseDTO.getFlightClass(),FlightClass.BUSINESS);
    }

    @Test
    void getTicketTicketNotFound() throws CustomNotFound {
        String id = "1";
        CustomNotFound e = assertThrows(CustomNotFound.class,() -> {
            ticketService.getTicket(id);
        });

        assertEquals(e.getValue(),id);
        assertEquals(e.getField(),"ticketId");
    }

    @Test
    void getTickeyByTicketNo() {
    }

    public Flight createFlight(){
        Flight flight = new Flight();
        flight.setId("1");
        flight.setPriceBusiness(300.0);
        flight.setPriceEconomic(140.0);
        flight.setCapasityBusiness(50);
        flight.setCapasityEconomic(100);
        flight.setSeatsEmpty();

        flight.setDepartureDate(LocalDateTime.of(2020,04,13,10,15));
        flight.setArrivalDate(LocalDateTime.of(2020,04,13,14,0));
        flight.setDuration("--");
        Airport departure = new Airport();
        departure.setName("Sabiha Gökçen");
        departure.setCity("İstanbul");
        departure.setId("1");
        departure.setArrivalFlights(Arrays.asList(flight));
        flight.setDeparture(departure);

        Airport arrival = new Airport();
        arrival.setName("Adnan Menderes");
        arrival.setCity("İzmir");
        arrival.setId("2");
        arrival.setDepartureFlights(Arrays.asList(flight));
        flight.setArrival(arrival);

        return flight;
    }

    private Passanger createPassanger() {
        Passanger passanger = new Passanger();
        passanger.setFullName("Hasan Cerit");
        passanger.setDateOfBirth(LocalDate.of(1998,6,10));
        passanger.setGender(Gender.male);
        passanger.setIdenityNo("21004730120");
        passanger.setPhoneNumber("05077348983");
        return passanger;
    }

}