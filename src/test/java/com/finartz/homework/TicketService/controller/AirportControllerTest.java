package com.finartz.homework.TicketService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finartz.homework.TicketService.dto.request.AirportRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirportResponseDTO;
import com.finartz.homework.TicketService.exception.exception.CustomNotFound;
import com.finartz.homework.TicketService.service.AirportService;
import com.finartz.homework.TicketService.util.SearchType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AirportControllerTest {
    private MockMvc mockMvc;

    @Mock
    private AirportService airportService;

    @InjectMocks
    private AirportController airportController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(airportController)
                .build();
    }

    @Test
    void saveAirport() throws Exception {
        AirportRequestDTO airportRequestDTO = new AirportRequestDTO("Sabiha Gökçen","İstanbul");
        AirportResponseDTO airportResponseDTO = new AirportResponseDTO("1","Sabiha Gökçen","İstanbul");

        when(airportService.saveAirport(airportRequestDTO)).thenReturn(airportResponseDTO);

        mockMvc.perform(
                post("/airport/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(airportRequestDTO)))
                .andExpect(status().isOk());

        verify(airportService, times(1)).saveAirport(airportRequestDTO);
    }

    @Test
    void saveAirportWithoutName() throws Exception {
        AirportRequestDTO airportRequestDTO = new AirportRequestDTO("","İstanbul");

        mockMvc.perform(
                post("/airport/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(airportRequestDTO)))
                .andExpect(status().isBadRequest());

        verify(airportService, times(0)).saveAirport(airportRequestDTO);
    }

    @Test
    void updateAirport() throws Exception {
        String id = "1";
        AirportRequestDTO airportRequestDTO = new AirportRequestDTO("Sabiha Gökçen","İstanbul");
        AirportResponseDTO airportResponseDto = new AirportResponseDTO(id,"Sabiha Gökçen","İstanbul");

        when(airportService.updateAirport(id,airportRequestDTO)).thenReturn(airportResponseDto);

        mockMvc.perform(
                post("/airport/update/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(airportRequestDTO)))
                .andExpect(status().isOk());
        verify(airportService, times(1)).updateAirport(id,airportRequestDTO);

    }

    @Test
    void updateAirportWithoutCity() throws Exception {
        String id = "1";
        AirportRequestDTO airportRequestDTO = new AirportRequestDTO("Sabiha Gökçen","");

        mockMvc.perform(
                post("/airport/update/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(airportRequestDTO)))
                .andExpect(status().isBadRequest());
        verify(airportService, times(0)).updateAirport(id,airportRequestDTO);

    }

    @Test
    void deleteAirport() throws Exception {
        String id = "1";

        doNothing().when(airportService).deleteAirport(id);

        mockMvc.perform(
                delete("/airport/"+id))
                .andExpect(status().isOk());
        verify(airportService, times(1)).deleteAirport(id);
    }

    @Test
    void getAll() throws Exception {
        List<AirportResponseDTO> airports = Arrays.asList(
                new AirportResponseDTO("1","Sabiha Gökçen","İstanbul"),
                new AirportResponseDTO("2","Adnan Menderes","İzmir"));

        when(airportService.getAll()).thenReturn(airports);

        mockMvc.perform(
                get("/airport/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].name", is("Sabiha Gökçen")))
                .andExpect(jsonPath("$[0].city", is("İstanbul")))
                .andExpect(jsonPath("$[1].id", is("2")))
                .andExpect(jsonPath("$[1].name", is("Adnan Menderes")))
                .andExpect(jsonPath("$[1].city", is("İzmir")));

        verify(airportService, times(1)).getAll();
    }

    @Test
    void getAirport() throws Exception {
        String id = "1";
        AirportResponseDTO airportResponseDTO = new AirportResponseDTO(id,"Sabiha Gökçen","İstanbul");

        when(airportService.getAirport(id)).thenReturn(airportResponseDTO);

        mockMvc.perform(
                get("/airport/{id}",id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.name", is("Sabiha Gökçen")))
                .andExpect(jsonPath("$.city", is("İstanbul")));

        verify(airportService, times(1)).getAirport(id);
    }

    @Test
    void getAirports() throws Exception {
        SearchType searchType = SearchType.bynameorcity;
        String nameOrCityValue = "sa";

        List<AirportResponseDTO> airports = Arrays.asList(
                new AirportResponseDTO("1","Sabiha Gökçen","İstanbul"),
                new AirportResponseDTO("2","Kırkpınar Havaalanı","Sakarya"));

        when(airportService.getAirports(searchType,nameOrCityValue)).thenReturn(airports);

        mockMvc.perform(
                get("/airport/search").param("type",searchType.toString()).param("value",nameOrCityValue))
                .andExpect(status().isOk());
        verify(airportService, times(1)).getAirports(searchType,nameOrCityValue);

    }

    @Test
    void getAirportsWithoutValueParam() throws Exception {
        SearchType searchType = SearchType.bynameorcity;
        mockMvc.perform(
                get("/airport/search").param("type",searchType.toString()))
                .andExpect(status().isBadRequest());
        verify(airportService, times(0)).getAirports(any(),any());

    }

    private String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}