package com.finartz.homework.TicketService.controller;

import com.finartz.homework.TicketService.dto.request.AirlineRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirlineResponseDTO;
import com.finartz.homework.TicketService.exception.exception.ApiException;
import com.finartz.homework.TicketService.service.AirlineService;
import com.finartz.homework.TicketService.service.FlightService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AirlineControllerTest {
    @Mock
    AirlineService airlineService;
    @Mock
    FlightService flightService;

    @InjectMocks
    AirlineController airlineController;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMockMvc() throws Exception{

    }

    @Test
    public void saveAirline() throws ApiException {
        AirlineRequestDTO airlineRequestDTO = new AirlineRequestDTO();
        airlineRequestDTO.setName("THY");

        AirlineResponseDTO airlineResponseDTO = new AirlineResponseDTO();
        airlineResponseDTO.setName("THY");

        when(airlineService.saveAirline(airlineRequestDTO)).thenReturn(airlineResponseDTO);

        AirlineResponseDTO responseDTO = airlineController.saveAirline(airlineRequestDTO).getBody();
        assertEquals(responseDTO.getName(),"THY");

        verify(airlineService,times(1)).saveAirline(airlineRequestDTO);
    }
}