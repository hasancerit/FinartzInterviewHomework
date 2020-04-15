package com.finartz.homework.TicketService.service;

import com.finartz.homework.TicketService.domain.Airline;
import com.finartz.homework.TicketService.domain.Airport;
import com.finartz.homework.TicketService.domain.Flight;
import com.finartz.homework.TicketService.domain.embeddable.Seat;
import com.finartz.homework.TicketService.dto.request.FlightRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirlineResponseDTO;
import com.finartz.homework.TicketService.dto.response.AirportResponseDTO;
import com.finartz.homework.TicketService.dto.response.FlightResponseDTO;
import com.finartz.homework.TicketService.dto.response.wrapper.FlightsResponseDTO;
import com.finartz.homework.TicketService.exception.exception.CustomAlreadyTaken;
import com.finartz.homework.TicketService.exception.exception.CustomNotFound;
import com.finartz.homework.TicketService.repositories.AirlineRepository;
import com.finartz.homework.TicketService.repositories.AirportRepository;
import com.finartz.homework.TicketService.repositories.FlightRepository;
import com.finartz.homework.TicketService.repositories.TicketRepository;
import com.finartz.homework.TicketService.service.imp.FlightServiceImpl;
import com.finartz.homework.TicketService.service.imp.TicketServiceImpl;
import com.finartz.homework.TicketService.util.SearchType;
import com.finartz.homework.TicketService.util.SeatStatus;
import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FlightServiceTest {
    @Mock
    private FlightRepository flightRepository;
    @Mock
    private AirportRepository airportRepository;
    @Mock
    private AirlineRepository airlineRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private ModelMapper modelMapper;

    FlightServiceImpl flightService;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        flightService = new FlightServiceImpl(flightRepository, airportRepository, airlineRepository, ticketRepository, modelMapper);
    }

    @Test
    void saveFlight() throws CustomAlreadyTaken, CustomNotFound {
        FlightRequestDTO flightRequestDTO = generateFlightRequestDTO("1", "13", "39");

        Airline airline = new Airline("1", "Thy", "");
        Airport departureAirport = new Airport("13", "Sabiha Gökçen", "İstanbul", "");
        Airport arrivalAirport = new Airport("39", "Adnan Menderes", "İzmir", "");

        given(airlineRepository.findById("1")).willReturn(Optional.of(airline));
        given(airportRepository.findById("13")).willReturn(Optional.of(departureAirport));
        given(airportRepository.findById("39")).willReturn(Optional.of(arrivalAirport));

        given(flightRepository.save(any())).willReturn(generateFlight(flightRequestDTO, airline, departureAirport, arrivalAirport));

        FlightResponseDTO flightResponseDTO = flightService.saveFlight(flightRequestDTO);

        assertEquals(flightResponseDTO.getAirline().getId(), "1");
        assertEquals(flightResponseDTO.getAirline().getName(), "Thy");

        assertEquals(flightResponseDTO.getDeparture().getId(), "13");
        assertEquals(flightResponseDTO.getDeparture().getName(), "Sabiha Gökçen");
        assertEquals(flightResponseDTO.getArrival().getId(), "39");
        assertEquals(flightResponseDTO.getArrival().getName(), "Adnan Menderes");
    }

    @Test
    void saveFlightSameCity() throws CustomAlreadyTaken, CustomNotFound {
        FlightRequestDTO flightRequestDTO = generateFlightRequestDTO("1", "13", "39");

        Airline airline = new Airline("1", "Thy", "");
        Airport departureAirport = new Airport("13", "Sabiha Gökçen", "İstanbul", "");
        Airport arrivalAirport = new Airport("39", "Atatürk", "İstanbul", "");

        given(airlineRepository.findById("1")).willReturn(Optional.of(airline));
        given(airportRepository.findById("13")).willReturn(Optional.of(departureAirport));
        given(airportRepository.findById("39")).willReturn(Optional.of(arrivalAirport));

        CustomAlreadyTaken e = assertThrows(CustomAlreadyTaken.class, () -> {
            flightService.saveFlight(flightRequestDTO);
        });

        assertEquals(e.getMessage(), "Cannot fly between the same cities");
        assertEquals(e.getField(), "departureAirportId,arrivalAirportId");
        assertEquals(e.getValue(), flightRequestDTO.getAirlineId());
    }

    @Test
    void saveFlightAirportNotFound() throws CustomAlreadyTaken, CustomNotFound {
        FlightRequestDTO flightRequestDTO = generateFlightRequestDTO("1", "13", "39");

        Airline airline = new Airline("1", "Thy", "");
        Airport departureAirport = new Airport("13", "Sabiha Gökçen", "İstanbul", "");

        given(airlineRepository.findById("1")).willReturn(Optional.of(airline));
        given(airportRepository.findById("13")).willReturn(Optional.of(departureAirport));

        CustomNotFound e = assertThrows(CustomNotFound.class, () -> {
            flightService.saveFlight(flightRequestDTO);
        });

        assertEquals(e.getField(), "arrivalId");
        assertEquals(e.getValue(), flightRequestDTO.getAirlineId());
    }

    @Test
    void updateFlight() throws CustomAlreadyTaken, CustomNotFound {
        //Update'den once
        FlightRequestDTO oldFlightDto = generateFlightRequestDTO("1", "13", "39");
        Airline airline = new Airline("1", "Thy", "");
        Airport departureAirport = new Airport("13", "Sabiha Gökçen", "İstanbul", "");
        Airport arrivalAirport = new Airport("39", "Adnan Menderes", "İzmir", "");
        Flight oldFlight = generateFlight(oldFlightDto, airline, departureAirport, arrivalAirport);

        given(flightRepository.findById("1")).willReturn(Optional.of(oldFlight));

        //Guncellenmek istenen hali
        FlightRequestDTO flightRequestDTO = modelMapper.map(oldFlightDto, FlightRequestDTO.class);
        flightRequestDTO.setAirlineId("2");

        Airline updateAirline = new Airline("2", "Pegasus", "");

        given(airlineRepository.findById("2")).willReturn(Optional.of(updateAirline));
        given(airportRepository.findById("13")).willReturn(Optional.of(departureAirport));
        given(airportRepository.findById("39")).willReturn(Optional.of(arrivalAirport));

        Flight updatedFlight = generateFlight(flightRequestDTO, updateAirline, departureAirport, arrivalAirport);
        given(flightRepository.save(any())).willReturn(updatedFlight);
        FlightResponseDTO flightResponseDTO = flightService.updateFlight("1", flightRequestDTO);

        assertEquals(flightResponseDTO.getAirline().getId(), "2");
        assertEquals(flightResponseDTO.getAirline().getName(), "Pegasus");
    }

    @Test
    void updateFlightNotFoundFlight() throws CustomAlreadyTaken, CustomNotFound {
        //Update'den once
        FlightRequestDTO oldFlightDto = generateFlightRequestDTO("1", "13", "39");


        //Guncellenmek istenen hali
        FlightRequestDTO flightRequestDTO = modelMapper.map(oldFlightDto, FlightRequestDTO.class);
        flightRequestDTO.setAirlineId("2");

        CustomNotFound e = assertThrows(CustomNotFound.class, () -> {
            flightService.updateFlight("1", flightRequestDTO);
        });

        assertEquals(e.getField(), "flightId");
        assertEquals(e.getValue(), "1");
    }

    @Test
    void deleteFlight() {
    }

    @Test
    void getAll() {
        Airline airline = new Airline("1", "Thy", "");
        Airline airline2 = new Airline("2", "Pegasus", "");

        Airport airport = new Airport("1", "Sabiha Gökçen", "İstanbul", "");
        Airport airport2 = new Airport("2", "Atatürk", "İstanbul", "");
        Airport airport3 = new Airport("3", "Süleyman Demirel", "Isparta", "");
        Airport airport4 = new Airport("4", "Adnan Menderes", "İzmir", "");


        List<Flight> flights = Arrays.asList(
                generateFlight(generateFlightRequestDTO("1", "1", "3"), airline, airport, airport3),
                generateFlight(generateFlightRequestDTO("2", "4", "2"), airline2, airport4, airport2),
                generateFlight(generateFlightRequestDTO("2", "3", "1"), airline2, airport3, airport)
        );

        given(flightRepository.findAll()).willReturn(flights);

        List<FlightResponseDTO> flightResponseDTOS = flightService.getAll();

        assertEquals(flightResponseDTOS.get(0).getAirline().getName(), "Thy");
        assertEquals(flightResponseDTOS.get(0).getDeparture().getName(), "Sabiha Gökçen");

        assertEquals(flightResponseDTOS.get(1).getAirline().getName(), "Pegasus");
        assertEquals(flightResponseDTOS.get(1).getArrival().getName(), "Atatürk");

        assertEquals(flightResponseDTOS.get(2).getDeparture().getName(), "Süleyman Demirel");
        assertEquals(flightResponseDTOS.get(2).getArrival().getName(), "Sabiha Gökçen");
    }

    @Test
    void getFlight() throws CustomNotFound {
        String id = "1";
        Airline airline = new Airline("1", "Thy", "");

        Airport departure = new Airport("12", "Sabiha Gökçen", "İstanbul", "");
        Airport arrival = new Airport("34", "Adnan Menderes", "İzmir", "");

        Flight flight = generateFlight(
                generateFlightRequestDTO("1", "12", "34"),
                airline,
                departure,
                arrival
        );

        given(flightRepository.findById(id)).willReturn(Optional.of(flight));
        FlightResponseDTO flightResponseDTO = flightService.getFlight(id);

        assertEquals(flightResponseDTO.getAirline().getId(), "1");
        assertEquals(flightResponseDTO.getAirline().getName(), "Thy");

        assertEquals(flightResponseDTO.getDeparture().getId(), "12");
        assertEquals(flightResponseDTO.getDeparture().getName(), "Sabiha Gökçen");
        assertEquals(flightResponseDTO.getArrival().getId(), "34");
        assertEquals(flightResponseDTO.getArrival().getName(), "Adnan Menderes");
    }

    @Test
    void getFlightNotFound() {
        String id = "1";

        CustomNotFound e = assertThrows(CustomNotFound.class, () -> {
            flightService.getFlight(id);
        });

        assertEquals(e.getField(), "flightId");
        assertEquals(e.getValue(), id);
    }

    @Test
    void getFlightsByAirlineName() throws CustomNotFound {
        String nameValue = "Th";
        Airline airline = new Airline("1", "Thy", "");

        Airport airport = new Airport("1", "Sabiha Gökçen", "İstanbul", "");
        Airport airport2 = new Airport("2", "Atatürk", "İstanbul", "");
        Airport airport3 = new Airport("3", "Süleyman Demirel", "Isparta", "");
        Airport airport4 = new Airport("4", "Adnan Menderes", "İzmir", "");

        airline.setActiveFlights(Arrays.asList(
                generateFlight(generateFlightRequestDTO("1", "1", "3"), airline, airport, airport3),
                generateFlight(generateFlightRequestDTO("1", "4", "2"), airline, airport4, airport2),
                generateFlight(generateFlightRequestDTO("1", "3", "1"), airline, airport3, airport)
        ));

        given(airlineRepository.findByNameIsContainingIgnoreCase(nameValue)).willReturn(Arrays.asList(airline));

        List<FlightResponseDTO> flightResponseDTOS = flightService.getFlightsByAirlineName(nameValue);

        assertEquals(flightResponseDTOS.get(0).getAirline().getName(), "Thy");
        assertEquals(flightResponseDTOS.get(0).getDeparture().getName(), "Sabiha Gökçen");

        assertEquals(flightResponseDTOS.get(1).getAirline().getName(), "Thy");
        assertEquals(flightResponseDTOS.get(1).getArrival().getName(), "Atatürk");

        assertEquals(flightResponseDTOS.get(2).getAirline().getName(), "Thy");
        assertEquals(flightResponseDTOS.get(2).getDeparture().getName(), "Süleyman Demirel");
        assertEquals(flightResponseDTOS.get(2).getArrival().getName(), "Sabiha Gökçen");
    }

    @Test
    void getFlightsByAirlineNameAirlineNotFound() throws CustomNotFound {
        String nameValue = "Th";

        CustomNotFound e = assertThrows(CustomNotFound.class, () -> {
            flightService.getFlightsByAirlineName(nameValue);
        });

        assertEquals(e.getField(), "airlineName");
        assertEquals(e.getValue(), nameValue);
    }

    @Test
    void getFlightsByDepartureByNameOrCity() throws CustomNotFound {
        SearchType searchType = SearchType.bynameorcity;
        String nameOrCityValue = "Ad";
        Airline airline = new Airline("1", "Thy", "");

        //Arama Bu ikisini kapsar
        Airport airport = new Airport("1", "Şakirpaşa", "Adana", "");
        Airport airport2 = new Airport("2", "Adnan Menderes", "Aydın", "");

        Airport airport3 = new Airport("3", "Süleyman Demirel", "Isparta", "");
        Airport airport4 = new Airport("4", "Adnan Menderes", "İzmir", "");

        //Adanadan gelecek ucuslar
        airport.setDepartureFlights(Arrays.asList(
                generateFlight(generateFlightRequestDTO("1", "1", "3"), airline, airport, airport3),
                generateFlight(generateFlightRequestDTO("1", "1", "2"), airline, airport, airport2),
                generateFlight(generateFlightRequestDTO("1", "1", "4"), airline, airport, airport4)
         ));

        //Adnan Menderesten gelecek ucuslar
        airport2.setDepartureFlights(Arrays.asList(
                generateFlight(generateFlightRequestDTO("1", "2", "1"), airline, airport2, airport),
                generateFlight(generateFlightRequestDTO("1", "2", "4"), airline, airport2, airport4),
                generateFlight(generateFlightRequestDTO("1", "2", "3"), airline, airport2, airport3)
        ));

        given(airportRepository.findByNameOrCity(nameOrCityValue)).willReturn(Arrays.asList(airport,airport2));

        List<FlightResponseDTO> flightResponseDTOS = flightService.getFlightsByDeparture(searchType,nameOrCityValue);

        assertEquals(flightResponseDTOS.get(0).getDeparture().getCity(),"Adana");
        assertEquals(flightResponseDTOS.get(1).getDeparture().getCity(),"Adana");
        assertEquals(flightResponseDTOS.get(2).getDeparture().getCity(),"Adana");

        assertEquals(flightResponseDTOS.get(3).getDeparture().getName(),"Adnan Menderes");
        assertEquals(flightResponseDTOS.get(4).getDeparture().getName(),"Adnan Menderes");
        assertEquals(flightResponseDTOS.get(5).getDeparture().getName(),"Adnan Menderes");



    }

    @Test
    void getFlightsByDepartureByName() throws CustomNotFound {
        SearchType searchType = SearchType.byname;
        String nameOrCityValue = "Ad";
        Airline airline = new Airline("1", "Thy", "");

        Airport airport = new Airport("1", "Şakirpaşa", "Adana", "");
        //Arama sadece bunu kapsar
        Airport airport2 = new Airport("2", "Adnan Menderes", "Aydın", "");
        Airport airport3 = new Airport("3", "Süleyman Demirel", "Isparta", "");
        Airport airport4 = new Airport("4", "Adnan Menderes", "İzmir", "");

        //Adnan Menderesten gelecek ucuslar
        airport2.setDepartureFlights(Arrays.asList(
                generateFlight(generateFlightRequestDTO("1", "2", "1"), airline, airport2, airport),
                generateFlight(generateFlightRequestDTO("1", "2", "4"), airline, airport2, airport4),
                generateFlight(generateFlightRequestDTO("1", "2", "3"), airline, airport2, airport3)
        ));

        given(airportRepository.findByNameIsContainingIgnoreCase(nameOrCityValue)).willReturn(Arrays.asList(airport2));

        List<FlightResponseDTO> flightResponseDTOS = flightService.getFlightsByDeparture(searchType,nameOrCityValue);

        assertEquals(flightResponseDTOS.get(0).getDeparture().getName(),"Adnan Menderes");
        assertEquals(flightResponseDTOS.get(1).getDeparture().getName(),"Adnan Menderes");
        assertEquals(flightResponseDTOS.get(2).getDeparture().getName(),"Adnan Menderes");
    }

    @Test
    void getFlightsByDepartureNotFound(){
        SearchType searchType = SearchType.bynameorcity;
        String nameOrCityValue = "Ad";

        CustomNotFound e = assertThrows(CustomNotFound.class,()->{
            flightService.getFlightsByDeparture(searchType,nameOrCityValue);
        });

        assertEquals(e.getField(), "value");
        assertEquals(e.getValue(), nameOrCityValue);
    }

    @Test
    void getFlightsByArrivalByNameOrCity() throws CustomNotFound {
        SearchType searchType = SearchType.bynameorcity;
        String nameOrCityValue = "Ad";
        Airline airline = new Airline("1", "Thy", "");

        //Arama Bu ikisini kapsar
        Airport airport = new Airport("1", "Şakirpaşa", "Adana", "");
        Airport airport2 = new Airport("2", "Adnan Menderes", "Aydın", "");

        Airport airport3 = new Airport("3", "Süleyman Demirel", "Isparta", "");
        Airport airport4 = new Airport("4", "Adnan Menderes", "İzmir", "");

        //Adana'ya inecek ucuslar
        airport.setArrivalFlights(Arrays.asList(
                generateFlight(generateFlightRequestDTO("1", "1", "3"), airline, airport3, airport),
                generateFlight(generateFlightRequestDTO("1", "1", "2"), airline, airport, airport),
                generateFlight(generateFlightRequestDTO("1", "1", "4"), airline, airport4, airport)
        ));

        //Adnan Menderesten gelecek ucuslar
        airport2.setArrivalFlights(Arrays.asList(
                generateFlight(generateFlightRequestDTO("1", "2", "1"), airline, airport4, airport2),
                generateFlight(generateFlightRequestDTO("1", "2", "4"), airline, airport3, airport2),
                generateFlight(generateFlightRequestDTO("1", "2", "3"), airline, airport, airport2)
        ));

        given(airportRepository.findByNameOrCity(nameOrCityValue)).willReturn(Arrays.asList(airport,airport2));

        List<FlightResponseDTO> flightResponseDTOS = flightService.getFlightsByArrival(searchType,nameOrCityValue);

        assertEquals(flightResponseDTOS.get(0).getArrival().getCity(),"Adana");
        assertEquals(flightResponseDTOS.get(1).getArrival().getCity(),"Adana");
        assertEquals(flightResponseDTOS.get(2).getArrival().getCity(),"Adana");

        assertEquals(flightResponseDTOS.get(3).getArrival().getName(),"Adnan Menderes");
        assertEquals(flightResponseDTOS.get(4).getArrival().getName(),"Adnan Menderes");
        assertEquals(flightResponseDTOS.get(5).getArrival().getName(),"Adnan Menderes");



    }

    @Test
    void getFlightsByArrivalByName() throws CustomNotFound {
        SearchType searchType = SearchType.byname;
        String nameOrCityValue = "Ad";
        Airline airline = new Airline("1", "Thy", "");

        Airport airport = new Airport("1", "Şakirpaşa", "Adana", "");
        //Arama sadece bunu kapsar
        Airport airport2 = new Airport("2", "Adnan Menderes", "Aydın", "");
        Airport airport3 = new Airport("3", "Süleyman Demirel", "Isparta", "");
        Airport airport4 = new Airport("4", "Adnan Menderes", "İzmir", "");

        //Adnan Menderesten gelecek ucuslar
        airport2.setArrivalFlights(Arrays.asList(
                generateFlight(generateFlightRequestDTO("1", "2", "1"), airline, airport, airport2),
                generateFlight(generateFlightRequestDTO("1", "2", "4"), airline, airport4, airport2),
                generateFlight(generateFlightRequestDTO("1", "2", "3"), airline, airport3, airport2)
        ));

        given(airportRepository.findByNameIsContainingIgnoreCase(nameOrCityValue)).willReturn(Arrays.asList(airport2));

        List<FlightResponseDTO> flightResponseDTOS = flightService.getFlightsByArrival(searchType,nameOrCityValue);

        assertEquals(flightResponseDTOS.get(0).getArrival().getName(),"Adnan Menderes");
        assertEquals(flightResponseDTOS.get(1).getArrival().getName(),"Adnan Menderes");
        assertEquals(flightResponseDTOS.get(2).getArrival().getName(),"Adnan Menderes");
    }

    @Test
    void getFlightsByArrivalNotFound(){
        SearchType searchType = SearchType.bynameorcity;
        String nameOrCityValue = "Ad";

        CustomNotFound e = assertThrows(CustomNotFound.class,()->{
            flightService.getFlightsByArrival(searchType,nameOrCityValue);
        });

        assertEquals(e.getField(), "value");
        assertEquals(e.getValue(), nameOrCityValue);
    }

    @Test
    void getFlightsByDepartureAndArrivalByCity() throws CustomNotFound{
        SearchType searchType = SearchType.bycity;
        String departureValue = "İstanbul";
        String arrivalValue = "İzmir";


        Airline airline = new Airline("1", "Thy", "");

        Airport airport = new Airport("1", "Sabiha Gökçen", "İstanbul", ""); //Departure
        Airport airport2 = new Airport("2", "Çıldır", "Aydın", "");
        Airport airport4 = new Airport("4", "Adnan Menderes", "İzmir", "");  //Arrival

        //Direkt istanbuldan izmire
        Flight directFlight1 = generateFlight(generateFlightRequestDTO("1","1","4"),airline,airport,airport4);
        Flight directFlight2 = generateFlight(generateFlightRequestDTO("1","1","4"),airline,airport,airport4);

        //İstanbuldan kalkar ama aydına gider.
        Flight indirectFlight11 = generateFlight(generateFlightRequestDTO("1","1","2"),airline,airport,airport2);
        indirectFlight11.setDepartureDate(LocalDateTime.of(2020,4,15,8,0));
        indirectFlight11.setArrivalDate(LocalDateTime.of(2020,4,15,12,0));

        //Aydından kalkar izmire gider.
        Flight indirectFlight12 = generateFlight(generateFlightRequestDTO("1","2","4"),airline,airport2,airport4);
        indirectFlight12.setDepartureDate(LocalDateTime.of(2020,4,15,15,0));
        indirectFlight12.setArrivalDate(LocalDateTime.of(2020,4,15,17,0));

        given(flightRepository.findByDepartureAndArrivalCity(departureValue,arrivalValue)).willReturn(Arrays.asList(directFlight1,directFlight2));

        //İstanbul'dan kalkanlar
        airport.setDepartureFlights(Arrays.asList(directFlight1,directFlight2,indirectFlight11));
        //İzmire inenler
        airport4.setArrivalFlights(Arrays.asList(directFlight1,directFlight2,indirectFlight12));
        //Aydından Kalkanlar
        airport2.setDepartureFlights(Collections.singletonList(indirectFlight12));
        //Aydına inenler
        airport2.setArrivalFlights(Collections.singletonList(indirectFlight11));

        given(airportRepository.findByCityIsContainingIgnoreCase(departureValue)).willReturn(Collections.singletonList(airport));

        FlightsResponseDTO flightsResponseDTO = flightService.getFlightsByDepartureAndArrival(searchType,departureValue,arrivalValue);

        assertEquals(flightsResponseDTO.getDirectFlights().size(),2);
        assertEquals(flightsResponseDTO.getIndirectFlights().size(),1);
    }

    @Test
    void getFlightsByDepartureAndArrivalByName() throws CustomNotFound{
        SearchType searchType = SearchType.byname;
        String departureValue = "Sabiha Gökçen";
        String arrivalValue = "Adnan Menderes";


        Airline airline = new Airline("1", "Thy", "");

        Airport airport = new Airport("1", "Sabiha Gökçen", "İstanbul", ""); //Departure
        Airport airport2 = new Airport("2", "Çıldır", "Aydın", "");
        Airport airport4 = new Airport("4", "Adnan Menderes", "İzmir", "");  //Arrival

        //Direkt istanbuldan izmire
        Flight directFlight1 = generateFlight(generateFlightRequestDTO("1","1","4"),airline,airport,airport4);
        Flight directFlight2 = generateFlight(generateFlightRequestDTO("1","1","4"),airline,airport,airport4);

        //İstanbuldan kalkar ama aydına gider.
        Flight indirectFlight11 = generateFlight(generateFlightRequestDTO("1","1","2"),airline,airport,airport2);
        indirectFlight11.setDepartureDate(LocalDateTime.of(2020,4,15,8,0));
        indirectFlight11.setArrivalDate(LocalDateTime.of(2020,4,15,12,0));

        //Aydından kalkar izmire gider.
        Flight indirectFlight12 = generateFlight(generateFlightRequestDTO("1","2","4"),airline,airport2,airport4);
        indirectFlight12.setDepartureDate(LocalDateTime.of(2020,4,15,15,0));
        indirectFlight12.setArrivalDate(LocalDateTime.of(2020,4,15,17,0));

        given(flightRepository.findByDepartureAndArrivalName(departureValue,arrivalValue)).willReturn(Arrays.asList(directFlight1,directFlight2));

        //İstanbul'dan kalkanlar
        airport.setDepartureFlights(Arrays.asList(directFlight1,directFlight2,indirectFlight11));
        //İzmire inenler
        airport4.setArrivalFlights(Arrays.asList(directFlight1,directFlight2,indirectFlight12));
        //Aydından Kalkanlar
        airport2.setDepartureFlights(Collections.singletonList(indirectFlight12));
        //Aydına inenler
        airport2.setArrivalFlights(Collections.singletonList(indirectFlight11));

        given(airportRepository.findByNameIsContainingIgnoreCase(departureValue)).willReturn(Collections.singletonList(airport));

        FlightsResponseDTO flightsResponseDTO = flightService.getFlightsByDepartureAndArrival(searchType,departureValue,arrivalValue);

        assertEquals(flightsResponseDTO.getDirectFlights().size(),2);
        assertEquals(flightsResponseDTO.getIndirectFlights().size(),1);
    }

    @Test
    void getFlightsByDepartureAndArrivalByNameOrCity() throws CustomNotFound{
        SearchType searchType = SearchType.bynameorcity;
        String departureValue = "Sabiha Gökçen";
        String arrivalValue = "İzmir";


        Airline airline = new Airline("1", "Thy", "");

        Airport airport = new Airport("1", "Sabiha Gökçen", "İstanbul", ""); //Departure
        Airport airport2 = new Airport("2", "Çıldır", "Aydın", "");
        Airport airport4 = new Airport("4", "Adnan Menderes", "İzmir", "");  //Arrival

        //Direkt istanbuldan izmire
        Flight directFlight1 = generateFlight(generateFlightRequestDTO("1","1","4"),airline,airport,airport4);
        Flight directFlight2 = generateFlight(generateFlightRequestDTO("1","1","4"),airline,airport,airport4);

        //İstanbuldan kalkar ama aydına gider.
        Flight indirectFlight11 = generateFlight(generateFlightRequestDTO("1","1","2"),airline,airport,airport2);
        indirectFlight11.setDepartureDate(LocalDateTime.of(2020,4,15,8,0));
        indirectFlight11.setArrivalDate(LocalDateTime.of(2020,4,15,12,0));

        //Aydından kalkar izmire gider.
        Flight indirectFlight12 = generateFlight(generateFlightRequestDTO("1","2","4"),airline,airport2,airport4);
        indirectFlight12.setDepartureDate(LocalDateTime.of(2020,4,15,15,0));
        indirectFlight12.setArrivalDate(LocalDateTime.of(2020,4,15,17,0));

        given(flightRepository.findByDepartureAndArrivalCityOrName(departureValue,arrivalValue)).willReturn(Arrays.asList(directFlight1,directFlight2));

        //İstanbul'dan kalkanlar
        airport.setDepartureFlights(Arrays.asList(directFlight1,directFlight2,indirectFlight11));
        //İzmire inenler
        airport4.setArrivalFlights(Arrays.asList(directFlight1,directFlight2,indirectFlight12));
        //Aydından Kalkanlar
        airport2.setDepartureFlights(Collections.singletonList(indirectFlight12));
        //Aydına inenler
        airport2.setArrivalFlights(Collections.singletonList(indirectFlight11));

        given(airportRepository.findByNameOrCity(departureValue)).willReturn(Collections.singletonList(airport));

        FlightsResponseDTO flightsResponseDTO = flightService.getFlightsByDepartureAndArrival(searchType,departureValue,arrivalValue);

        assertEquals(flightsResponseDTO.getDirectFlights().size(),2);
        assertEquals(flightsResponseDTO.getIndirectFlights().size(),1);
    }


    /**
     * FlightRequestDto cok alanlı bir sınıf old icin,kod kalabalıgini engellemek adina, sadece airlineId,departureAirportId
     * ve arrivalAirportId'sinin belirlendigi, diger alanlarin rastgele(Servis katmanında hata olsuturmayacak bicimde) olusturularak
     * dondurulecegi metod.
     *
     * @param airlineId          Olusturulacak flightRequestDto icin istenen airlineId
     * @param departureAirportId Olusturulacak flightRequestDto icin istenen departureAirportId
     * @param arrivalAirportId   Olusturulacak flightRequestDto icin istenen arrivalAirportId
     * @return flightRequestDto gonderilen parametreler harici, doldurularak doner.
     */
    private FlightRequestDTO generateFlightRequestDTO(String airlineId, String departureAirportId, String arrivalAirportId) {
        FlightRequestDTO flight = new FlightRequestDTO();
        Random random = new Random();

        LocalDateTime time1 = null;
        LocalDateTime time2 = null;
        int day = 1 + random.nextInt(2);
        do {
            int hour = 1 + random.nextInt(18);
            int minute = 1 + random.nextInt(59);

            int hour2 = 1 + random.nextInt(18);
            int minute2 = 1 + random.nextInt(59);

            time1 = LocalDateTime.of(2020, 4, day, hour, minute);
            time2 = LocalDateTime.of(2020, 4, day, hour2, minute2);
        } while (time2.isBefore(time1));

        /*Degerini bizim belirledigimiz alanlar*/
        flight.setAirlineId(airlineId);
        flight.setDepartureAirportId(departureAirportId);
        flight.setArrivalAirportId(arrivalAirportId);

        /*Degeri rastgele(fakat anlamlı) olusturuldugu alanlar*/
        flight.setDepartureDate(time1);
        flight.setArrivalDate(time2);
        flight.setCapasityEconomic(60 + random.nextInt(200));
        flight.setCapasityBusiness(flight.getCapasityEconomic() + random.nextInt(100));
        flight.setPriceEconomic(Double.valueOf(100 + random.nextInt(300)));
        flight.setPriceBusiness(Double.valueOf(flight.getPriceEconomic() + random.nextInt(300)));

        return flight;
    }

    /**
     * @return repo'dan donecek flight üret.
     */
    private Flight generateFlight(FlightRequestDTO flightRequestDTO, Airline airline, Airport departure, Airport arrival) {
        Flight flight = modelMapper.map(flightRequestDTO, Flight.class);

        flight.setAirline(airline);
        flight.setDeparture(departure);
        flight.setArrival(arrival);

        flight.setSeatsEmpty();
        flight.setDuration("-");

        return flight;
    }
}