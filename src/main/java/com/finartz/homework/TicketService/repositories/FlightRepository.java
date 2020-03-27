package com.finartz.homework.TicketService.repositories;

import com.finartz.homework.TicketService.domain.Airline;
import com.finartz.homework.TicketService.domain.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlightRepository extends JpaRepository<Flight,String> {
    List<Flight> findByAirline_NameIsContainingIgnoreCase(String airlineName);


    List<Flight> findByDeparture_NameContainingIgnoreCase(String departureName);

    List<Flight> findByDeparture_CityContainingIgnoreCase(String departureCity);

    List<Flight> findByDeparture_CityContainingIgnoreCaseOrDeparture_NameContainingIgnoreCase(String departureName,String departureCity);


    List<Flight> findByArrival_NameContainingIgnoreCase(String arrivalName);

    List<Flight> findByArrival_CityContainingIgnoreCase(String arrivalCity);

    List<Flight> findByArrival_CityContainingIgnoreCaseOrDeparture_NameContainingIgnoreCase(String arrivalNameOrCity, String arrivalNameOrCity1);
}
