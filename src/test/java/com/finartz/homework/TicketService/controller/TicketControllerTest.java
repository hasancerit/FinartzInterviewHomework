package com.finartz.homework.TicketService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.finartz.homework.TicketService.domain.embeddable.Passanger;
import com.finartz.homework.TicketService.dto.request.TicketRequestDTO;
import com.finartz.homework.TicketService.dto.response.FlightResponseDTO;
import com.finartz.homework.TicketService.dto.response.TicketResponseDTO;
import com.finartz.homework.TicketService.service.TicketService;
import com.finartz.homework.TicketService.util.FlightClass;
import com.finartz.homework.TicketService.util.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TicketControllerTest {
    private MockMvc mockMvc;

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketController ticketController;

    //Testlerde, TicketRequestDto'da kullanılacak Passanger nesnesi
    private Passanger passanger;
    //Testlerde, TicketResposeDto'dan donecek Flight nesnesi.
    private FlightResponseDTO flight;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(ticketController)
                .build();

        passanger = new Passanger();
        passanger.setFullName("Hasan Cerit");
        passanger.setDateOfBirth(LocalDate.of(1998,6,10));
        passanger.setGender(Gender.male);
        passanger.setIdenityNo("21004730120");
        passanger.setPhoneNumber("05077348983");

        flight = new FlightResponseDTO();
        flight.setId("1");
    }

    @Test
    void saveTicket() throws Exception {
        TicketRequestDTO ticketRequestDTO = new TicketRequestDTO(flight.getId(), FlightClass.BUSINESS,passanger,"29");
        TicketResponseDTO ticketResponseDTO = new TicketResponseDTO("1","9T12B5",passanger,flight,FlightClass.BUSINESS,"29");
        when(ticketService.saveTicket(ticketRequestDTO)).thenReturn(ticketResponseDTO);

        mockMvc.perform(
                post("/ticket/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(ticketRequestDTO)))
                .andExpect(status().isOk());
        verify(ticketService, times(1)).saveTicket(ticketRequestDTO);
    }

    @Test
    void saveTicketWithoutPassanger() throws Exception {
        TicketRequestDTO ticketRequestDTO = new TicketRequestDTO(flight.getId(), FlightClass.BUSINESS,null,"29");
        mockMvc.perform(
                post("/ticket/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(ticketRequestDTO)))
                .andExpect(status().isBadRequest());
        verify(ticketService, times(0)).saveTicket(ticketRequestDTO);

    }

    @Test
    void updateTicket() throws Exception {
        String ticketId = "1";
        TicketRequestDTO ticketRequestDTO = new TicketRequestDTO(flight.getId(), FlightClass.BUSINESS,passanger,"34");
        TicketResponseDTO ticketResponseDTO = new TicketResponseDTO(ticketId,"9T12S5",passanger,flight,FlightClass.BUSINESS,"34");

        when(ticketService.updateTicket(ticketId,ticketRequestDTO)).thenReturn(ticketResponseDTO);

        mockMvc.perform(
                post("/ticket/update/{id}",ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(ticketRequestDTO)))
                .andExpect(status().isOk());
        verify(ticketService, times(1)).updateTicket(ticketId,ticketRequestDTO);
    }

    @Test
    void updateTicketInvalidNo() throws Exception {
        String ticketId = "1";
        TicketRequestDTO ticketRequestDTO = new TicketRequestDTO(flight.getId(), FlightClass.BUSINESS,passanger,"iki");

        mockMvc.perform(
                post("/ticket/update/{id}",ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(ticketRequestDTO)))
                .andExpect(status().isBadRequest());
        verify(ticketService, times(0)).updateTicket(ticketId,ticketRequestDTO);

    }

    @Test
    void deleteTicket() throws Exception {
        String id = "1";

        doNothing().when(ticketService).deleteTicket(id);

        mockMvc.perform(
                delete("/ticket/"+id))
                .andExpect(status().isOk());
        verify(ticketService, times(1)).deleteTicket(id);
    }

    @Test
    void getAll() throws Exception {
        List<TicketResponseDTO> tickets = Arrays.asList(
                new TicketResponseDTO("1","9T12B5",passanger,flight,FlightClass.BUSINESS,"29"),
                new TicketResponseDTO("2","8C14D3",passanger,flight,FlightClass.ECONOMI,"3"));
        when(ticketService.getAll()).thenReturn(tickets);

        mockMvc.perform(
                get("/ticket/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].ticketNo", is("9T12B5")))
                .andExpect(jsonPath("$[1].id", is("2")))
                .andExpect(jsonPath("$[1].ticketNo", is("8C14D3")));
        verify(ticketService, times(1)).getAll();
    }

    @Test
    void getTicket() throws Exception {
        String id = "5";
        TicketResponseDTO ticketResponseDTO = new TicketResponseDTO(id,"9T12B5",passanger,flight,FlightClass.BUSINESS,"29");

        when(ticketService.getTicket(id)).thenReturn(ticketResponseDTO);

        mockMvc.perform(
                get("/ticket/{id}",id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.no", is("29")));

        verify(ticketService, times(1)).getTicket(id);
    }

    @Test
    void getTicketByTicketNo() throws Exception{
        String ticketNo = "9T12B5";
        TicketResponseDTO ticketResponseDTO = new TicketResponseDTO("1",ticketNo,passanger,flight,FlightClass.BUSINESS,"29");

        when(ticketService.getTickeyByTicketNo(ticketNo)).thenReturn(ticketResponseDTO);

        mockMvc.perform(
                get("/ticket/pnr").param("pnr",ticketNo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.no", is("29")))
                .andExpect(jsonPath("$.ticketNo", is(ticketNo)));

        verify(ticketService, times(1)).getTickeyByTicketNo(ticketNo);
    }

    @Test
    void getTicketByTicketNoWithoutParam() throws Exception{
        mockMvc.perform(
                get("/ticket/pnr"))
                .andExpect(status().isBadRequest());
        verify(ticketService, times(0)).getTickeyByTicketNo(any());

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