package com.finartz.homework.TicketService.service.imp;

import com.finartz.homework.TicketService.domain.Airline;
import com.finartz.homework.TicketService.domain.Airport;
import com.finartz.homework.TicketService.domain.Flight;
import com.finartz.homework.TicketService.dto.request.FlightRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirportResponseDTO;
import com.finartz.homework.TicketService.dto.response.FlightResponseDTO;
import com.finartz.homework.TicketService.dto.response.FlightsResponseDTO;
import com.finartz.homework.TicketService.repositories.AirlineRepository;
import com.finartz.homework.TicketService.repositories.AirportRepository;
import com.finartz.homework.TicketService.repositories.FlightRepository;
import com.finartz.homework.TicketService.service.FlightService;
import com.finartz.homework.TicketService.util.SearchType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FlightServiceImpl implements FlightService {
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private AirportRepository airportRepository;
    @Autowired
    private AirlineRepository airlineRepository;
    @Autowired
    private ModelMapper modelMapper;

    /**Ekleme**/
    @Override
    public FlightResponseDTO saveFlight(FlightRequestDTO flightDto) {
        Flight flight = modelMapper.map(flightDto,Flight.class);

        /*Bunları ModelMapper Conf ile yapabilir miyim?*/
        flight.setDeparture(airportRepository.getOne(flightDto.getDepartureAirportId()));
        flight.setArrival(airportRepository.getOne(flightDto.getArrivalAirportId()));
        flight.setAirline(airlineRepository.getOne(flightDto.getAirlineId()));

        flightRepository.save(flight);
        return modelMapper.map(flight,FlightResponseDTO.class);
    }

    @Override
    public List<FlightResponseDTO> getAll() {
        return flightListToFlightResponseDtoList(flightRepository.findAll());
    }


    /**Id ile Arama**/
    @Override
    public FlightResponseDTO getFlight(String id){
        return modelMapper.map(flightRepository.getOne(id),FlightResponseDTO.class);
    }

    /**Havayolu İsmi ile Arama.**/
    @Override
    public List<FlightResponseDTO> getFlightsByAirlineName(String airlineName) {
        List<FlightResponseDTO> flightResponseDTOList = new ArrayList<>();
        List<Airline> airlines = airlineRepository.findByNameIsContainingIgnoreCase(airlineName);
        airlines.stream().forEach(airport -> { //Aramaya uygun her bir havayolu
            airport.getActiveFlights().stream().forEach(flight -> {  //Ve bu havayolu kalkan her bir uçak
                flightResponseDTOList.add(modelMapper.map(flight,FlightResponseDTO.class));  //Listeye ekle(Dönüstürerek)
            });
        });
        return flightResponseDTOList;
    }

    /**Kalkış Havaalanına göre Arama(value'den kalkan ucuslar)**/
    @Override
    public List<FlightResponseDTO> getFlightsByDeparture(SearchType searchType,String nameOrCity) {
        if(searchType == SearchType.byName){        /*Kalkış Havaalanı İsmi ile Arama*/
            return getDepartureFlightResponseDtoListFromAirportList(
                    airportRepository.findByNameIsContainingIgnoreCase(nameOrCity));
        }else if(searchType == SearchType.byCity){  /*Kalkış Havaalanı Şehiri ile Arama*/
            return getDepartureFlightResponseDtoListFromAirportList(
                    airportRepository.findByCityIsContainingIgnoreCase(nameOrCity));
        }else{                                      /*Kalkış Havaalanı Şehiri VEYA ismi ile Arama*/
            return getDepartureFlightResponseDtoListFromAirportList(
                    airportRepository.findByNameIgnoreCaseIsContainingOrCityIgnoreCaseIsContaining(nameOrCity,nameOrCity));
        }
    }

    /**Varış Havaalanına göre Arama(value'ye inen ucuslar)**/
    @Override
    public List<FlightResponseDTO> getFlightsByArrival(SearchType searchType,String nameOrCity) {
        if(searchType == SearchType.byName){        /*Kalkış Havaalanı İsmi ile Arama*/
            return getArrivalFlightResponseDtoListFromAirportList(
                    airportRepository.findByNameIsContainingIgnoreCase(nameOrCity));
        }else if(searchType == SearchType.byCity){  /*Kalkış Havaalanı Şehiri ile Arama*/
            return getArrivalFlightResponseDtoListFromAirportList(
                    airportRepository.findByCityIsContainingIgnoreCase(nameOrCity));
        }else{                                      /*Kalkış Havaalanı Şehiri VEYA ismi ile Arama*/
            return getArrivalFlightResponseDtoListFromAirportList(
                    airportRepository.findByNameIgnoreCaseIsContainingOrCityIgnoreCaseIsContaining(nameOrCity,nameOrCity));
        }
    }

    /**Kalkış Havaalanı ve İniş Havaalanına göre arama**/
    @Override
    public FlightsResponseDTO getFlightsByDepartureAndArrival(SearchType searchType, String departure, String arrival) {
        FlightsResponseDTO result = new FlightsResponseDTO();
        List<FlightResponseDTO> aktarmasiz;
        List<FlightResponseDTO> aktarmali;
        if(searchType == SearchType.byName){
            aktarmasiz = flightListToFlightResponseDtoList(flightRepository.findByDepartureAndArrivalName(departure,arrival));
            result.setAktarmasizUcuslar(aktarmasiz);
            result.setAktarmaliUcuslar(null);
        }else if(searchType == SearchType.byCity){
            aktarmasiz = flightListToFlightResponseDtoList(flightRepository.findByDepartureAndArrivalCity(departure,arrival));
            result.setAktarmasizUcuslar(aktarmasiz);
            result.setAktarmaliUcuslar(null);
        }else{
            aktarmasiz = flightListToFlightResponseDtoList(flightRepository.findByDepartureAndArrivalCityOrName(departure,arrival));
            result.setAktarmasizUcuslar(aktarmasiz);
            result.setAktarmaliUcuslar(null);
        }

        return result;
    }


    private List<FlightResponseDTO> getArrivalFlightResponseDtoListFromAirportList(List<Airport> airports){
        List<FlightResponseDTO> flightResponseDTOList = new ArrayList<>();
        airports.stream().forEach(airport -> { //Aramaya uygun her bir havaalanı
            airport.getArrivalFlights().stream().forEach(flight -> {  //Ve bu havaalanından kalkan her bir uçak
                flightResponseDTOList.add(modelMapper.map(flight,FlightResponseDTO.class));  //Listeye ekle(Dönüstürerek)
            });
        });
        return flightResponseDTOList;
    }
    private List<FlightResponseDTO> getDepartureFlightResponseDtoListFromAirportList(List<Airport> airports){
        List<FlightResponseDTO> flightResponseDTOList = new ArrayList<>();
        airports.stream().forEach(airport -> { //Aramaya uygun her bir havaalanı
            airport.getDepartureFlights().stream().forEach(flight -> {  //Ve bu havaalanına inen her bir uçak
                flightResponseDTOList.add(modelMapper.map(flight,FlightResponseDTO.class));  //Listeye ekle(Dönüstürerek)
            });
        });
        return flightResponseDTOList;
    }
    private List<FlightResponseDTO> flightListToFlightResponseDtoList(List<Flight> flights){
        List<FlightResponseDTO> flightResponseDTOList = new ArrayList<>();
            flights.stream().forEach(flight -> {  //Her bir flight
                flightResponseDTOList.add(modelMapper.map(flight,FlightResponseDTO.class));  //Listeye ekle(Dönüstürerek)
            });
        return flightResponseDTOList;
    }
}