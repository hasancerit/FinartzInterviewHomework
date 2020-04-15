package com.finartz.homework.TicketService.service;

import com.finartz.homework.TicketService.domain.Airport;
import com.finartz.homework.TicketService.dto.request.AirportRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirportResponseDTO;
import com.finartz.homework.TicketService.exception.exception.CustomAlreadyTaken;
import com.finartz.homework.TicketService.exception.exception.CustomNotFound;
import com.finartz.homework.TicketService.repositories.AirportRepository;
import com.finartz.homework.TicketService.service.imp.AirportServiceImpl;
import com.finartz.homework.TicketService.util.SearchType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
class AirportServiceTest {
    @Mock
    private AirportRepository airportRepository;
    @Mock
    private ModelMapper modelMapper;

    private AirportServiceImpl airportService;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        airportService = new AirportServiceImpl(airportRepository,modelMapper);
    }

    @Test
    void saveAirport() throws CustomAlreadyTaken {
        AirportRequestDTO airportRequestDTO =  new AirportRequestDTO("Sabiha Gökçen","İstanbul"); //Kaydedilmek Istenen DTO

        /*Vt'ye kaydolup donecek hali*/
        Airport airport = modelMapper.map(airportRequestDTO,Airport.class);
        airport.setId("1");

        //airlineDomain geldiginde, airline'a dondur.
        given(airportRepository.save(any())).willReturn(airport);

        //Kaydedilecek DTO'yu gonder.
        AirportResponseDTO airportResponseDTO= airportService.saveAirport(airportRequestDTO);

        assertEquals(airportResponseDTO.getName(),"Sabiha Gökçen");
        assertEquals(airportResponseDTO.getCity(),"İstanbul");
        assertEquals(airportResponseDTO.getId(),"1");
    }

    @Test
    void updateAirport() throws CustomAlreadyTaken, CustomNotFound {
        String id = "1";                                                               //Guncellenecek olan airportId
        Airport airportDomain = new Airport();                                         //Guncellenecek olan Domain
        airportDomain.setId("1");
        airportDomain.setName("Sabiha Gökçen");
        airportDomain.setName("İstanbul");

        given(airportRepository.findById(id)).willReturn(Optional.of(airportDomain));

        AirportRequestDTO updatedDto =  new AirportRequestDTO("Adnan Menderes","İzmir");       //Guncellenmek istenen hali

        /*Vt'ye kaydolup donecek hali*/
        Airport airport = modelMapper.map(updatedDto,Airport.class);
        airport.setId(id);

        //updatedDto geldiginde, airline'a dondur.
        given(airportRepository.save(any())).willReturn(airport);

        //Kaydedilecek DTO'yu gonder.
        AirportResponseDTO airportResponseDTO = airportService.updateAirport(id,updatedDto);

        assertEquals(airportResponseDTO.getId(),"1");
        assertEquals(airportResponseDTO.getName(),"Adnan Menderes");
        assertEquals(airportResponseDTO.getCity(),"İzmir");

    }


    @Test
    void updateAirportNotFound()  {
        String id = "1";                                                               //Guncellenecek olan airlineId
        Airport airportDomain = new Airport();                                         //Guncellenecek olan Domain
        airportDomain.setName("Sabiha Gökçen");
        airportDomain.setCity("İstanbul");
        airportDomain.setId("1");

        AirportRequestDTO updatedDto =  new AirportRequestDTO("Pegasus","Updated Desk");

        CustomNotFound e = assertThrows(CustomNotFound.class,() -> {
            airportService.updateAirport(id,updatedDto);
        });

        assertEquals(e.getField(),"airportId");
        assertEquals(e.getValue(),id);
    }

    @Test
    void deleteAirport() throws CustomNotFound {
        String id = "1";

        willDoNothing().given(airportRepository).deleteById(id);
        airportService.deleteAirport(id);
    }

    @Test
    void getAll() {
        List<AirportResponseDTO> airlineDtos = Arrays.asList(
                new AirportResponseDTO("1","Sabiha Gökçen","İstanbul"),
                new AirportResponseDTO("2","Adnan Menderes","İzmir"));

        List<Airport> airports = Arrays.asList(
                modelMapper.map(airlineDtos.get(0),Airport.class),
                modelMapper.map(airlineDtos.get(1),Airport.class));

        given(airportRepository.findAll()).willReturn(airports);

        List<AirportResponseDTO> airlineResponseDTOList = airportService.getAll();

        assertEquals(airlineResponseDTOList.get(0).getId(),"1");
        assertEquals(airlineResponseDTOList.get(0).getName(),"Sabiha Gökçen");
        assertEquals(airlineResponseDTOList.get(0).getCity(),"İstanbul");
        assertEquals(airlineResponseDTOList.get(1).getId(),"2");
        assertEquals(airlineResponseDTOList.get(1).getName(),"Adnan Menderes");
        assertEquals(airlineResponseDTOList.get(1).getCity(),"İzmir");

    }

    @Test
    void getAirport() throws CustomNotFound {
        String id = "1";
        AirportResponseDTO airportResponseDTO = new AirportResponseDTO(id,"Sabiha Gökçen","İstanbul");

        Airport airport = modelMapper.map(airportResponseDTO,Airport.class);
        given(airportRepository.findById(id)).willReturn(Optional.of(airport));

        AirportResponseDTO response = airportService.getAirport(id);

        assertEquals(response.getId(),"1");
        assertEquals(response.getName(),"Sabiha Gökçen");
        assertEquals(response.getCity(),"İstanbul");
    }


    @Test
    void getAirportNotFound(){
        String id = "1";

        CustomNotFound e = assertThrows(CustomNotFound.class,() -> {
            airportService.getAirport(id);
        });

        assertEquals(e.getField(),"airportId");
        assertEquals(e.getValue(),id);
    }

    @Test
    void getAirports() throws CustomNotFound {
        SearchType searchType = SearchType.bynameorcity;
        String nameOrCityValue = "ad";
        List<AirportResponseDTO> airlineDtos = Arrays.asList(
                new AirportResponseDTO("1","Şakirpaşa","Adana"),
                new AirportResponseDTO("2","Adnan Menderes","İzmir"));

        List<Airport> airports = Arrays.asList(
                modelMapper.map(airlineDtos.get(0),Airport.class),
                modelMapper.map(airlineDtos.get(1),Airport.class));

        given(airportRepository.findByNameOrCity(nameOrCityValue)).willReturn(airports);

        List<AirportResponseDTO> responses = airportService.getAirports(searchType,nameOrCityValue);

        assertEquals(responses.get(0).getId(),"1");
        assertEquals(responses.get(0).getName(),"Şakirpaşa");
        assertEquals(responses.get(0).getCity(),"Adana");
        assertEquals(responses.get(1).getId(),"2");
        assertEquals(responses.get(1).getName(),"Adnan Menderes");
        assertEquals(responses.get(1).getCity(),"İzmir");
    }

    @Test
    void getAirports404(){
        SearchType searchType = SearchType.bycity;
        String nameOrCityValue = "ney";

        given(airportRepository.findByCityIsContainingIgnoreCase(nameOrCityValue)).willReturn(null);

        CustomNotFound e = assertThrows(CustomNotFound.class,() -> {
            airportService.getAirports(searchType,nameOrCityValue);
        });

        assertEquals(e.getField(),"value");
        assertEquals(e.getObject(),nameOrCityValue.getClass());
        assertEquals(e.getValue(),nameOrCityValue);
    }
}