package com.finartz.homework.TicketService.service;

import  com.finartz.homework.TicketService.domain.Airline;
import com.finartz.homework.TicketService.dto.request.AirlineRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirlineResponseDTO;
import com.finartz.homework.TicketService.exception.exception.CustomAlreadyTaken;
import com.finartz.homework.TicketService.exception.exception.CustomNotFound;
import com.finartz.homework.TicketService.repositories.AirlineRepository;
import com.finartz.homework.TicketService.repositories.AirportRepository;
import com.finartz.homework.TicketService.service.imp.AirlineServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class AirlineServiceTest {

    @Mock
    private AirlineRepository airlineRepository;
    @Mock
    private AirportRepository airportRepository;
    @Mock
    private ModelMapper modelMapper;

    private AirlineServiceImpl airlineService;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        airlineService = new AirlineServiceImpl(airlineRepository,airportRepository,modelMapper);
    }

    @Test
    void saveAirline() throws CustomAlreadyTaken {
        AirlineRequestDTO airlineRequestDto =  new AirlineRequestDTO("Thy");    //Kaydedilmek Istenen DTO

        /*Vt'ye kaydolup donecek hali*/
        Airline airline = modelMapper.map(airlineRequestDto,Airline.class);
        airline.setId("1");

        //airlineDomain geldiginde, airline'a dondur.
        given(airlineRepository.save(any())).willReturn(airline);

        //Kaydedilecek DTO'yu gonder.
        AirlineResponseDTO airlineResponseDto = airlineService.saveAirline(airlineRequestDto);

        assertEquals(airlineResponseDto.getName(),"Thy");
        assertEquals(airlineResponseDto.getId(),"1");

    }

    @Test
    void updateAirline() throws CustomAlreadyTaken, CustomNotFound {
        String id = "1";                                                               //Guncellenecek olan airlineId
        Airline airlineDomain = new Airline();                                         //Guncellenecek olan Domain
        airlineDomain.setName("Thy");
        airlineDomain.setId("1");

        given(airlineRepository.findById(id)).willReturn(Optional.of(airlineDomain));

        AirlineRequestDTO updatedDto =  new AirlineRequestDTO("Pegasus","Updated Desk");       //Guncellenmek istenen hali

        /*Vt'ye kaydolup donecek hali*/
        Airline airline = modelMapper.map(updatedDto,Airline.class);
        airline.setId(id);

        //updatedDto geldiginde, airline'a dondur.
        given(airlineRepository.save(any())).willReturn(airline);

        //Kaydedilecek DTO'yu gonder.
        AirlineResponseDTO airlineResponseDto = airlineService.updateAirline(id,updatedDto);

        assertEquals(airlineResponseDto.getId(),"1");
        assertEquals(airlineResponseDto.getName(),"Pegasus");
        assertEquals(airlineResponseDto.getDesc(),"Updated Desk");

    }


    @Test
    void deleteAirline() {
    }

    @Test
    void getAll() {
        List<AirlineResponseDTO> airlineDtos = Arrays.asList(
                new AirlineResponseDTO("1","Thy"),
                new AirlineResponseDTO("2","Pegasus"));

        List<Airline> airlines = Arrays.asList(
                modelMapper.map(airlineDtos.get(0),Airline.class),
                modelMapper.map(airlineDtos.get(1),Airline.class));

        given(airlineRepository.findAll()).willReturn(airlines);

        List<AirlineResponseDTO> airlineResponseDTOList = airlineService.getAll();

        assertEquals(airlineResponseDTOList.get(0).getId(),"1");
        assertEquals(airlineResponseDTOList.get(0).getName(),"Thy");
        assertEquals(airlineResponseDTOList.get(1).getId(),"2");
        assertEquals(airlineResponseDTOList.get(1).getName(),"Pegasus");
    }

    @Test
    void getAirline() throws CustomNotFound {
        String id = "1";
        AirlineResponseDTO airlineResponseDTO = new AirlineResponseDTO(id,"Thy");

        Airline airline = modelMapper.map(airlineResponseDTO,Airline.class);
        given(airlineRepository.findById(id)).willReturn(Optional.of(airline));

        AirlineResponseDTO response = airlineService.getAirline(id);

        assertEquals(response.getId(),"1");
        assertEquals(response.getName(),"Thy");
    }

    @Test
    void getAirlinesByName() throws CustomNotFound {
        String name = "th";
        List<AirlineResponseDTO> airlineDtos = Arrays.asList(new AirlineResponseDTO("1","Thy"));

        Airline airline = modelMapper.map(airlineDtos.get(0),Airline.class);
        given(airlineRepository.findByNameIsContainingIgnoreCase(name)).willReturn(Arrays.asList(airline));

        List<AirlineResponseDTO> responses = airlineService.getAirlinesByName(name);

        assertEquals(responses.get(0).getId(),"1");
        assertEquals(responses.get(0).getName(),"Thy");
    }

    @Test
    void getAirlinesByName404() throws CustomNotFound {
        String name = "fad";
        given(airlineRepository.findByNameIsContainingIgnoreCase(name)).willReturn(null);

        CustomNotFound e = assertThrows(CustomNotFound.class,() -> {
            airlineService.getAirlinesByName(name);
        });

        assertEquals(e.getField(),"name");
        assertEquals(e.getObject(),name.getClass());
        assertEquals(e.getValue(),name);
    }
}