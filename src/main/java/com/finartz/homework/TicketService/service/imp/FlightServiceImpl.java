package com.finartz.homework.TicketService.service.imp;

import com.finartz.homework.TicketService.domain.Airline;
import com.finartz.homework.TicketService.domain.Airport;
import com.finartz.homework.TicketService.domain.Flight;
import com.finartz.homework.TicketService.dto.request.FlightRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirportResponseDTO;
import com.finartz.homework.TicketService.dto.response.wrapper.IndirectFlightDTO;
import com.finartz.homework.TicketService.dto.response.FlightResponseDTO;
import com.finartz.homework.TicketService.dto.response.wrapper.FlightsResponseDTO;
import com.finartz.homework.TicketService.exception.exception.ArrivalBeforeDepartureException;
import com.finartz.homework.TicketService.exception.exception.ApiException;
import com.finartz.homework.TicketService.repositories.AirlineRepository;
import com.finartz.homework.TicketService.repositories.AirportRepository;
import com.finartz.homework.TicketService.repositories.FlightRepository;
import com.finartz.homework.TicketService.service.FlightService;
import com.finartz.homework.TicketService.util.SearchType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
    public FlightResponseDTO saveFlight(FlightRequestDTO flightDto) throws ArrivalBeforeDepartureException, ApiException {
        Flight flight = modelMapper.map(flightDto,Flight.class);
        flight.setSeatsEmpty();

        /*Fix!*/
        try{
            flight.setAirline(airlineRepository.findById(flightDto.getAirlineId()).get());
        }catch (NoSuchElementException ex){
            throw new ApiException("airlineId Not Found",flightDto.getClass(),"airlineId",flightDto.getAirlineId());
        }
        try{
            flight.setDeparture(airportRepository.findById(flightDto.getDepartureAirportId()).get());
        }catch (NoSuchElementException ex){
            throw new ApiException("departureId Not Found",flightDto.getClass(),"departureId",flightDto.getAirlineId());
        }
        try{
            flight.setArrival(airportRepository.findById(flightDto.getArrivalAirportId()).get());
        }catch (NoSuchElementException ex){
            throw new ApiException("arrivalId Not Found",flightDto.getClass(),"arrivalId",flightDto.getAirlineId());
        }
        /**/

        if(flight.getArrival().getCity().equalsIgnoreCase(flight.getDeparture().getCity()))
            throw new ApiException("Cannot fly between the same cities",
                    flightDto.getClass(),"departureAirportId,arrivalAirportId",flightDto.getAirlineId());

        if(flight.getArrivalDate().isBefore(flight.getDepartureDate()))
            throw new ArrivalBeforeDepartureException(flight.getArrivalDate(),flight.getDepartureDate());
        String duration = Duration.between(flight.getArrivalDate(), flight.getDepartureDate()).toString();
        flight.setDuration(duration.replace("-"," ").substring(3));


        flightRepository.save(flight);
        return modelMapper.map(flight,FlightResponseDTO.class);
    }

    @Override
    public List<FlightResponseDTO> getAll() {
        return flightListToFlightResponseDtoList(flightRepository.findAll());
    }

    @Override
    public void deleteFlight(String id) throws ApiException {
        Flight flight;
        try{
            flight = flightRepository.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new ApiException("FlightId Not Found",id.getClass(),"flightId",id);
        }
        flightRepository.delete(flight);
    }


    /**Id ile Arama**/
    @Override
    public FlightResponseDTO getFlight(String id){
        try{
            return modelMapper.map(flightRepository.findById(id).get(),FlightResponseDTO.class);
        }catch (NoSuchElementException ex){
            return null;
        }
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
        if(searchType == SearchType.byname){        /*Kalkış Havaalanı İsmi ile Arama*/
            return getDepartureFlightResponseDtoListFromAirportList(
                    airportRepository.findByNameIsContainingIgnoreCase(nameOrCity));
        }else if(searchType == SearchType.bycity){  /*Kalkış Havaalanı Şehiri ile Arama*/
            return getDepartureFlightResponseDtoListFromAirportList(
                    airportRepository.findByCityIsContainingIgnoreCase(nameOrCity));
        }else{                                      /*Kalkış Havaalanı Şehiri VEYA ismi ile Arama*/
            return getDepartureFlightResponseDtoListFromAirportList(
                    airportRepository.findByNameOrCity(nameOrCity,nameOrCity));
        }
    }

    /**Varış Havaalanına göre Arama(value'ye inen ucuslar)**/
    @Override
    public List<FlightResponseDTO> getFlightsByArrival(SearchType searchType,String nameOrCity) {
        if(searchType == SearchType.byname){        /*Kalkış Havaalanı İsmi ile Arama*/
            return getArrivalFlightResponseDtoListFromAirportList(
                    airportRepository.findByNameIsContainingIgnoreCase(nameOrCity));
        }else if(searchType == SearchType.bycity){  /*Kalkış Havaalanı Şehiri ile Arama*/
            return getArrivalFlightResponseDtoListFromAirportList(
                    airportRepository.findByCityIsContainingIgnoreCase(nameOrCity));
        }else{                                      /*Kalkış Havaalanı Şehiri VEYA ismi ile Arama*/
            return getArrivalFlightResponseDtoListFromAirportList(
                    airportRepository.findByNameOrCity(nameOrCity,nameOrCity));
        }
    }

    /**Kalkış Havaalanı ve İniş Havaalanına göre arama**/
    @Override
    public FlightsResponseDTO getFlightsByDepartureAndArrival(SearchType searchType, String departure, String arrival) {
        FlightsResponseDTO result = new FlightsResponseDTO();
        List<FlightResponseDTO> direct;
        if(searchType == SearchType.byname){        /*Kalkış Havaalanı İsmi ile Arama*/
            direct = flightListToFlightResponseDtoList(flightRepository.findByDepartureAndArrivalName(departure,arrival));
            result.setDirectFlights(direct);
            result.setIndirectFlights(getIndırectFlights(searchType,departure,arrival));
        }else if(searchType == SearchType.bycity){  /*Kalkış Havaalanı Şehiri ile Arama*/
            direct = flightListToFlightResponseDtoList(flightRepository.findByDepartureAndArrivalCity(departure,arrival));
            result.setDirectFlights(direct);
            result.setIndirectFlights(getIndırectFlights(searchType,departure,arrival));
        }else{                                      /*Kalkış Havaalanı Şehiri VEYA ismi ile Arama*/
            direct = flightListToFlightResponseDtoList(flightRepository.findByDepartureAndArrivalCityOrName(departure,arrival));
            result.setDirectFlights(direct);
            result.setIndirectFlights(getIndırectFlights(searchType,departure,arrival));
        }
        return result;
    }


    private List<IndirectFlightDTO> getIndırectFlights(SearchType searchType, String departure, String arrival){
        ArrayList<IndirectFlightDTO> indirectFlights = new ArrayList<>();
        List<FlightResponseDTO> possibleFirstFlights = this.getFlightsByDeparture(searchType,departure); //Olası ilk Ucuslar
        possibleFirstFlights.stream().forEach(possibleFirstFlight -> {
            AirportResponseDTO possibleArrival = possibleFirstFlight.getArrival(); //Olası ilk varis noktası
            possibleArrival.getDepartureFlights().stream().forEach(possibleSecondFlight->{ //Olası ilk varış noktasından kalkan ucusları al
                //Olası 2. ucusun varis noktası, istenen nokta'yı iceriyor ise;
                if(possibleSecondFlight.getArrival().getName().toUpperCase().contains(arrival.toUpperCase())){
                    if(possibleSecondFlight.getDepartureDate().isAfter(possibleFirstFlight.getArrivalDate())){ //2.nin kalkışı 1.den sonra ise
                        Duration duration = Duration.between(possibleFirstFlight.getArrivalDate(), possibleSecondFlight.getDepartureDate());
                        if(duration.toHours() <= 12){ //Yolcu 12 saatten fazla beklemeyecekse
                            indirectFlights.add(new IndirectFlightDTO(possibleFirstFlight,possibleSecondFlight));
                        }
                    }
                }
            });
        });
        return indirectFlights;
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