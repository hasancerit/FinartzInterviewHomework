package com.finartz.homework.TicketService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finartz.homework.TicketService.dto.request.AirlineRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirlineResponseDTO;
import com.finartz.homework.TicketService.service.AirlineService;
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
import static org.hamcrest.core.Is.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AirlineControllerTest {
    private MockMvc mockMvc;

    @Mock
    private AirlineService airlineService;

    @InjectMocks
    private AirlineController airlineController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(airlineController)
                .build();
    }

    @Test
    void saveAirline() throws Exception {
        AirlineRequestDTO airlineRequestDTO = new AirlineRequestDTO("thy");

        AirlineResponseDTO airlineResponseDTO = new AirlineResponseDTO("1","thy");

        when(airlineService.saveAirline(airlineRequestDTO)).thenReturn(airlineResponseDTO);

        mockMvc.perform(
                post("/airline/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(airlineRequestDTO)))
                .andExpect(status().isOk());
        verify(airlineService, times(1)).saveAirline(airlineRequestDTO);
    }

    @Test
    void saveAirlineWithoutName() throws Exception {
        AirlineRequestDTO airlineRequestDTO = new AirlineRequestDTO("");

        mockMvc.perform(
                post("/airline/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(airlineRequestDTO)))
                .andExpect(status().isBadRequest());
        verify(airlineService, times(0)).saveAirline(airlineRequestDTO);
    }

    @Test
    void updateAirline() throws Exception {
        String id = "1";
        AirlineRequestDTO airlineRequestDTO = new AirlineRequestDTO("updated name");

        AirlineResponseDTO airlineResponseDTO= new AirlineResponseDTO(id,"updated name");

        when(airlineService.updateAirline(id,airlineRequestDTO)).thenReturn(airlineResponseDTO);

        mockMvc.perform(
                post("/airline/update/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(airlineRequestDTO)))
                .andExpect(status().isOk());
        verify(airlineService, times(1)).updateAirline(id,airlineRequestDTO);
    }
    @Test
    void updateAirlineWithoutName() throws Exception {
        String id = "1";
        AirlineRequestDTO airlineRequestDTO = new AirlineRequestDTO("");

        mockMvc.perform(
                post("/airline/update/"+id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(airlineRequestDTO)))
                .andExpect(status().isBadRequest());
        verify(airlineService, times(0)).updateAirline(id,airlineRequestDTO);
    }

    @Test
    void deleteAirline() {
    }

    @Test
    void getAll() throws Exception {
        List<AirlineResponseDTO> airlines = Arrays.asList(
                new AirlineResponseDTO("1","Thy"),
                new AirlineResponseDTO("2","Pegasus"));

        when(airlineService.getAll()).thenReturn(airlines);

        mockMvc.perform(
                get("/airline/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].name", is("Thy")))
                .andExpect(jsonPath("$[1].id", is("2")))
                .andExpect(jsonPath("$[1].name", is("Pegasus")));
        verify(airlineService, times(1)).getAll();
    }

    @Test
    void getAirline() throws Exception {
        String id = "1";
        AirlineResponseDTO airlineResponseDTO = new AirlineResponseDTO("1","Thy");

        when(airlineService.getAirline(id)).thenReturn(airlineResponseDTO);

        mockMvc.perform(
                get("/airline/{id}",id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.name", is("Thy")));

        verify(airlineService, times(1)).getAirline(id);
    }

    @Test
    void getAirlinesByName() throws Exception {
        String searchValue = "th";
        List<AirlineResponseDTO> airlines = Arrays.asList(
                new AirlineResponseDTO("1","Thy"));

        when(airlineService.getAirlinesByName(searchValue)).thenReturn(airlines);

        mockMvc.perform(
                get("/airline/name").param("name",searchValue))
                .andExpect(status().isOk());
        verify(airlineService, times(1)).getAirlinesByName(searchValue);

    }
    @Test
    void getAirlinesWithoutParam() throws Exception {
        mockMvc.perform(
                get("/airline/name"))
                .andExpect(status().isBadRequest());
        verify(airlineService, times(0)).getAirlinesByName(any());

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