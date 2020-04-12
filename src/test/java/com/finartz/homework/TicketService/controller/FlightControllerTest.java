package com.finartz.homework.TicketService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.finartz.homework.TicketService.domain.embeddable.Seat;
import com.finartz.homework.TicketService.dto.request.FlightRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirlineResponseDTO;
import com.finartz.homework.TicketService.dto.response.AirportResponseDTO;
import com.finartz.homework.TicketService.dto.response.FlightResponseDTO;
import com.finartz.homework.TicketService.dto.response.TicketResponseDTO;
import com.finartz.homework.TicketService.dto.response.wrapper.FlightsResponseDTO;
import com.finartz.homework.TicketService.dto.response.wrapper.IndirectFlightDTO;
import com.finartz.homework.TicketService.exception.exception.ArrivalBeforeDepartureException;
import com.finartz.homework.TicketService.exception.exception.CustomAlreadyTaken;
import com.finartz.homework.TicketService.exception.exception.CustomNotFound;
import com.finartz.homework.TicketService.service.FlightService;
import com.finartz.homework.TicketService.util.FlightClass;
import com.finartz.homework.TicketService.util.SearchType;
import com.finartz.homework.TicketService.util.SeatStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FlightControllerTest {
    private MockMvc mockMvc;

    @Mock
    private FlightService flightService;

    @InjectMocks
    private FlightController flightController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(flightController)
                .build();
    }


    @Test
    void saveFlight() throws Exception {
        FlightRequestDTO flightRequestDTO = generateFlightRequestDTO("3","32","12");
        FlightResponseDTO flightResponseDTO = generateFlightResponseDTO("1",flightRequestDTO);

        when(flightService.saveFlight(flightRequestDTO)).thenReturn(flightResponseDTO);

        mockMvc.perform(
                post("/flight/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(flightRequestDTO)))
                .andExpect(status().isOk());
        verify(flightService, times(1)).saveFlight(flightRequestDTO);
    }

    @Test
    void saveFlightWithoutAirlineId() throws Exception {
        FlightRequestDTO flightRequestDTO = generateFlightRequestDTO("","32","12");
        FlightResponseDTO flightResponseDTO = generateFlightResponseDTO("1",flightRequestDTO);
        flightResponseDTO.setAirline(null);

        mockMvc.perform(
                post("/flight/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(flightRequestDTO)))
                .andExpect(status().isBadRequest());
        verify(flightService, times(0)).saveFlight(flightRequestDTO);
    }

    @Test
    void addFlightToAirline() throws Exception {
        String airlineId = "3";
        FlightRequestDTO flightRequestDTO = generateFlightRequestDTO(airlineId,"22","5");
        FlightResponseDTO flightResponseDTO = generateFlightResponseDTO("1",flightRequestDTO);

        when(flightService.saveFlight(flightRequestDTO)).thenReturn(flightResponseDTO);

        mockMvc.perform(
                post("/flight/add/toairline/{airlineId}",airlineId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(flightRequestDTO)))
                .andExpect(status().isOk());
        verify(flightService, times(1)).saveFlight(flightRequestDTO);
    }

    @Test
    void updateFlight() throws Exception {
        String flightId = "4";
        FlightRequestDTO flightRequestDTO = generateFlightRequestDTO("1","4","22");
        FlightResponseDTO flightResponseDTO = generateFlightResponseDTO(flightId,flightRequestDTO);

        when(flightService.updateFlight(flightId,flightRequestDTO)).thenReturn(flightResponseDTO);

        mockMvc.perform(
                post("/flight/update/{id}",flightId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(flightRequestDTO)))
                .andExpect(status().isOk());
        verify(flightService, times(1)).updateFlight(flightId,flightRequestDTO);
    }

    @Test
    void updateFlightWithoutAirlineId() throws Exception {
        String flightId = "4";
        FlightRequestDTO flightRequestDTO = generateFlightRequestDTO("1","4","");

        mockMvc.perform(
                post("/flight/update/{id}",flightId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(flightRequestDTO)))
                .andExpect(status().isBadRequest());
        verify(flightService, times(0)).updateFlight(flightId,flightRequestDTO);
    }


    @Test
    void deleteFlight() {
    }

    @Test
    void getAll() throws Exception {
        FlightResponseDTO flight1 = generateFlightResponseDTO("1",generateFlightRequestDTO("2","53","21"));
        FlightResponseDTO flight2 = generateFlightResponseDTO("2",generateFlightRequestDTO("2","12","42"));

        List<FlightResponseDTO> flights = Arrays.asList(flight1,flight2);

        when(flightService.getAll()).thenReturn(flights);

        mockMvc.perform(
                get("/flight/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[1].id", is("2")));
        verify(flightService, times(1)).getAll();
    }

    @Test
    void getFlight() throws Exception {
        String id = "2";
        FlightResponseDTO flightResponseDTO = generateFlightResponseDTO(id,generateFlightRequestDTO("2","4","22"));

        when(flightService.
                getFlight(id)).thenReturn(flightResponseDTO);

        mockMvc.perform(
                get("/flight/{id}",id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)));

        verify(flightService, times(1)).getFlight(id);
    }

    @Test
    void getFlightsByAirline() throws Exception {
        String airlineName = "Airline 3";
        FlightResponseDTO flightResponseDTO1 = generateFlightResponseDTO("8",generateFlightRequestDTO("3","11","23"));
        FlightResponseDTO flightResponseDTO2 = generateFlightResponseDTO("47",generateFlightRequestDTO("3","75","17"));
        List<FlightResponseDTO> flights = Arrays.asList(flightResponseDTO1,flightResponseDTO2);
        when(flightService.getFlightsByAirlineName(airlineName)).thenReturn(flights);

        mockMvc.perform(
                get("/flight/airline").param("name",airlineName))
                .andExpect(status().isOk());
        verify(flightService, times(1)).getFlightsByAirlineName(airlineName);
    }
    @Test
    void getFlightsByAirlineWithoutParam() throws Exception {
        mockMvc.perform(
                get("/flight/airline"))
                .andExpect(status().isBadRequest());
        verify(flightService, times(0)).getFlightsByAirlineName(anyString());
    }


    @Test
    void getFlightsByDeparture() throws Exception {
        SearchType searchType = SearchType.bynameorcity;
        String nameOrCityValue = "Airport 1";

        List<FlightResponseDTO> flights = Arrays.asList(
                generateFlightResponseDTO("3",generateFlightRequestDTO("3","41","9")),
                generateFlightResponseDTO("6",generateFlightRequestDTO("6","11","23"))
        );

        when(flightService.getFlightsByDeparture(searchType,nameOrCityValue)).thenReturn(flights);
        mockMvc.perform(
                get("/flight/departure")
                        .param("type",searchType.toString())
                        .param("value",nameOrCityValue))
                .andExpect(status().isOk());

        verify(flightService, times(1)).getFlightsByDeparture(searchType,nameOrCityValue);
    }

    @Test
    void getFlightsByDepartureWithoutDepartureParam() throws Exception {
        SearchType searchType = SearchType.bynameorcity;
        mockMvc.perform(
                get("/flight/departure")
                        .param("type",searchType.toString()))
                .andExpect(status().isBadRequest());

        verify(flightService, times(0)).getFlightsByDeparture(any(),anyString());
    }

    @Test
    void getFlightsByArrival() throws Exception {
        SearchType searchType = SearchType.bynameorcity;
        String nameOrCityValue = "Airport 1";

        List<FlightResponseDTO> flights = Arrays.asList(
                generateFlightResponseDTO("3",generateFlightRequestDTO("3","15","41")),
                generateFlightResponseDTO("6",generateFlightRequestDTO("6","11","11"))
        );

        when(flightService.getFlightsByArrival(searchType,nameOrCityValue)).thenReturn(flights);
        mockMvc.perform(
                get("/flight/arrival")
                        .param("type",searchType.toString())
                        .param("value",nameOrCityValue))
                .andExpect(status().isOk());

        verify(flightService, times(1)).getFlightsByArrival(searchType,nameOrCityValue);
    }

    @Test
    void getFlightsByArrivalWithoutDepartureParam() throws Exception {
        SearchType searchType = SearchType.bynameorcity;
        mockMvc.perform(
                get("/flight/arrival")
                        .param("type",searchType.toString()))
                .andExpect(status().isBadRequest());

        verify(flightService, times(0)).getFlightsByArrival(any(),anyString());
    }

    @Test
    void getFlightsByDepartureAndArrival() throws Exception {
        SearchType searchType = SearchType.bynameorcity;
        String departure = "Airport 1";
        String arrival = "Airport 4";

        List<FlightResponseDTO> flights = Arrays.asList(
                generateFlightResponseDTO("3",generateFlightRequestDTO("3","1","76")),
                generateFlightResponseDTO("6",generateFlightRequestDTO("6","32","4"))
        );

        FlightsResponseDTO result = new FlightsResponseDTO();
        result.setDirectFlights(flights);
        result.setIndirectFlights(null);

        when(flightService.getFlightsByDepartureAndArrival(searchType,departure,arrival)).thenReturn(result);
        mockMvc.perform(
                get("/flight/temp")
                        .param("type",searchType.toString())
                        .param("departure",departure)
                        .param("arrival",arrival))
                .andExpect(status().isOk());

        verify(flightService, times(1)).getFlightsByDepartureAndArrival(searchType,departure,arrival);
    }

    @Test
    void getFlightsByDepartureAndArrivalWithoutArrival() throws Exception {
        SearchType searchType = SearchType.bynameorcity;
        String departure = "Airport 1";

        mockMvc.perform(
                get("/flight/temp")
                        .param("type",searchType.toString())
                        .param("departure",departure))
                .andExpect(status().isBadRequest());

        verify(flightService, times(0)).getFlightsByDepartureAndArrival(any(),anyString(),anyString());
    }

    /**
     * FlightRequestDto cok alanlı bir sınıf old icin,kod kalabalıgini engellemek adina, sadece airlineId,departureAirportId
     *ve arrivalAirportId'sinin belirlendigi, diger alanlarin rastgele(Servis katmanında hata olsuturmayacak bicimde) olusturularak
     *dondurulecegi metod.
     *
     * @param airlineId             Olusturulacak flightRequestDto icin istenen airlineId
     * @param departureAirportId    Olusturulacak flightRequestDto icin istenen departureAirportId
     * @param arrivalAirportId      Olusturulacak flightRequestDto icin istenen arrivalAirportId
     * @return                      flightRequestDto gonderilen parametreler harici, doldurularak doner.
     */
    private FlightRequestDTO generateFlightRequestDTO(String airlineId, String departureAirportId,String arrivalAirportId){
        FlightRequestDTO flight = new FlightRequestDTO();
        Random random = new Random();

        LocalDateTime time1 = null;
        LocalDateTime time2 = null;
        int day = 1+random.nextInt(2);
        do{
            int hour = 1+random.nextInt(18);
            int minute = 1+random.nextInt(59);

            int hour2 = 1+random.nextInt(18);
            int minute2 = 1+random.nextInt(59);

            time1 = LocalDateTime.of(2020,4,day,hour,minute);
            time2 = LocalDateTime.of(2020,4,day,hour2,minute2);
        }while(time2.isBefore(time1));

        /*Degerini bizim belirledigimiz alanlar*/
        flight.setAirlineId(airlineId);
        flight.setDepartureAirportId(departureAirportId);
        flight.setArrivalAirportId(arrivalAirportId);

        /*Degeri rastgele(fakat anlamlı) olusturuldugu alanlar*/
        flight.setDepartureDate(time1);
        flight.setArrivalDate(time2);
        flight.setCapasityEconomic(60+random.nextInt(200));
        flight.setCapasityBusiness(flight.getCapasityEconomic() + random.nextInt(100));
        flight.setPriceEconomic(Double.valueOf(100+random.nextInt(300)));
        flight.setPriceBusiness(Double.valueOf(flight.getPriceEconomic()+random.nextInt(300)));

        return flight;
    }

    /**
     * FlightResponseDTO cok alanlı bir sınıf old icin,kod kalabalıgini engellemek adina, servis katmanının yapacagi gibi
     * flightRequest'ten flightResponse'a donusumu saglayan metod.
     *
     * @param flightRequestDTO  donusturulecek flightRequestDTO nesnesi
     * @return  donusturulen flightResponseDTO nesnesi
     */
    private FlightResponseDTO generateFlightResponseDTO(String flightId,FlightRequestDTO flightRequestDTO){
        FlightResponseDTO flightResponseDTO = new FlightResponseDTO();

        flightResponseDTO.setId(flightId);

        String airlineId = flightRequestDTO.getAirlineId();
        AirlineResponseDTO airline = new AirlineResponseDTO(
                airlineId,
                " Airline"+airlineId);
        flightResponseDTO.setAirline(airline);

        String departureId = flightRequestDTO.getDepartureAirportId();
        AirportResponseDTO departure = new AirportResponseDTO(
                departureId,
                "Airport Name"+departureId,
                "Airport City"+departureId);

        String arrivalId = flightRequestDTO.getDepartureAirportId();
        AirportResponseDTO arrival = new AirportResponseDTO(
                arrivalId,
                "Airport Name"+arrivalId,
                "Airport City"+arrivalId);

        flightResponseDTO.setDepartureDate(flightRequestDTO.getDepartureDate());
        flightResponseDTO.setArrivalDate(flightRequestDTO.getArrivalDate());

        String duration = Duration.between(flightResponseDTO.getArrivalDate(), flightResponseDTO.getDepartureDate()).toString();
        flightResponseDTO.setDuration(duration.replace("-", " ").substring(3));

        flightResponseDTO.setPriceBusiness(flightRequestDTO.getPriceBusiness());
        flightResponseDTO.setPriceEconomic(flightRequestDTO.getPriceEconomic());
        flightResponseDTO.setCapasityBusiness(flightRequestDTO.getCapasityBusiness());
        flightResponseDTO.setCapasityEconomic(flightRequestDTO.getCapasityEconomic());

        Map<String, Seat> seatsEconomic = new HashMap<>();
        Map<String, Seat> seatsBusiness = new HashMap<>();
        //Koltuklari default hale getir.
        for(int i = 1 ; i <= flightResponseDTO.getCapasityEconomic() ;  i++){
            seatsEconomic.put(""+i,new Seat(SeatStatus.empty,null));
        }
        for(int i = 1 ; i <= flightResponseDTO.getCapasityBusiness() ; i++){
            seatsBusiness.put(""+i,new Seat(SeatStatus.empty,null));
        }

        return flightResponseDTO;
    }

    private String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            final String jsonContent = mapper.writeValueAsString(obj);
            System.out.println(jsonContent);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}