package com.finartz.homework.TicketService.service.imp;

import com.finartz.homework.TicketService.domain.Airline;
import com.finartz.homework.TicketService.dto.response.AirlineResponseDTO;
import com.finartz.homework.TicketService.repositories.AirlineRepository;
import com.finartz.homework.TicketService.repositories.AirportRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class AirlineServiceImplTest {
    @Mock
    AirlineRepository airlineRepository;
    @Mock
    AirportRepository airportRepository;
    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    AirlineServiceImpl airlineService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Airline airline = new Airline();
        airline.setId("1");
        airline.setName("THY");
        airline.setDesc("THY Aciklama");
        airline.setActiveFlights(null);

        when(airlineRepository.findAll()).thenReturn(Arrays.asList(airline));
    }

    @Test
    public void saveAirline() {
    }

    @Test
    public void updateAirline() {
    }

    @Test
    public void deleteAirline() {
    }

    @Test
    public void getAll() {
        List<AirlineResponseDTO> airlines = airlineService.getAll();
        assertEquals(airlines.size(),1);
    }

    @Test
    public void getAirline() {
    }

    @Test
    public void getAirlinesByName() {
    }
}