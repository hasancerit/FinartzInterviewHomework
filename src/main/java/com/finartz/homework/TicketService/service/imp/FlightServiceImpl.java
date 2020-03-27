package com.finartz.homework.TicketService.service.imp;

import com.finartz.homework.TicketService.domain.Airline;
import com.finartz.homework.TicketService.domain.Airport;
import com.finartz.homework.TicketService.domain.Flight;
import com.finartz.homework.TicketService.dto.request.FlightRequestDTO;
import com.finartz.homework.TicketService.dto.response.FlightResponseDTO;
import com.finartz.homework.TicketService.repositories.AirlineRepository;
import com.finartz.homework.TicketService.repositories.AirportRepository;
import com.finartz.homework.TicketService.repositories.FlightRepository;
import com.finartz.homework.TicketService.service.FlightService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    /*Ekleme*/
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


    /*Id ile Arama*/
    @Override
    public FlightResponseDTO getFlight(String id){
        return modelMapper.map(flightRepository.getOne(id),FlightResponseDTO.class);
    }


    /*Havayolu İsmi ile Arama*/
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


    /**Kalkış Havaalanına göre Arama**/
    /*Kalkış Havaalanı İsmi ile Arama*/
    @Override
    public List<FlightResponseDTO> getFlightsByDepartureName(String departureName) {
        return getDepartureFlightResponseDtoListFromAirportList(
                airportRepository.findByNameIsContainingIgnoreCase(departureName));
    }

    /*Kalkış Havaalanı Şehiri ile Arama*/
    @Override
    public List<FlightResponseDTO> getFlightsByDepartureCity(String departureCity) {
        return getDepartureFlightResponseDtoListFromAirportList(
                airportRepository.findByCityIsContainingIgnoreCase(departureCity));
    }

    /*Kalkış Havaalanı Şehiri VEYA ismi ile Arama*/
    @Override
    public List<FlightResponseDTO> getFlightsByDepartureNameOrCity(String departureNameOrCity) {
        return getDepartureFlightResponseDtoListFromAirportList(
                airportRepository.findByNameIgnoreCaseIsContainingOrCityIgnoreCaseIsContaining(departureNameOrCity,departureNameOrCity));
    }


    /**Varış Havaalanına göre Arama**/
    /*Varış Havaalanı İsmi ile Arama*/
    @Override
    public List<FlightResponseDTO> getFlightsByArrivalName(String arrivalName) {
       return getArrivalFlightResponseDtoListFromAirportList(
               airportRepository.findByNameIsContainingIgnoreCase(arrivalName));

    }

    /*Varış Havaalanı Şehiri ile Arama*/
    @Override
    public List<FlightResponseDTO> getFlightsByArrivalCity(String arrivalCity) {
        return getArrivalFlightResponseDtoListFromAirportList(
                airportRepository.findByCityIsContainingIgnoreCase(arrivalCity));
    }

    /*Varış Havaalanı Şehiri VEYA ismi ile Arama*/
    @Override
    public List<FlightResponseDTO> getFlightsByArrivalNameOrCity(String arrivalNameOrCity) {
        return getArrivalFlightResponseDtoListFromAirportList(
                airportRepository.findByNameIgnoreCaseIsContainingOrCityIgnoreCaseIsContaining(arrivalNameOrCity,arrivalNameOrCity));
    }

    /**Kalkış Havaalanı ve İniş Havaalanına göre arama**/
    /*Kalkış Havaalanı isimi ve İniş Havaalanı isimi ile arama*/
    @Override
    public List<FlightResponseDTO> getFlightsByDepartureAndArrivalName(String departureName, String arrivalName) {
        return flightListToFlightResponseDtoList(
                flightRepository.findByDeparture_NameIsContainingIgnoreCaseAndArrival_NameIsContainingIgnoreCase(departureName,arrivalName));
    }

    /*Kalkış Havaalanı şehiri ve İniş Havaalanı şehiri ile arama*/
    @Override
    public List<FlightResponseDTO> getFlightsByDepartureAndArrivalCity(String departureCity, String arrivalCity) {
        return flightListToFlightResponseDtoList(
                flightRepository.findByDeparture_CityIsContainingIgnoreCaseAndArrival_CityIsContainingIgnoreCase(departureCity,arrivalCity));
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