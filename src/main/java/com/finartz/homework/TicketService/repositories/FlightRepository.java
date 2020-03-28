package com.finartz.homework.TicketService.repositories;

import com.finartz.homework.TicketService.domain.Airline;
import com.finartz.homework.TicketService.domain.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FlightRepository extends JpaRepository<Flight,String> {
    @Query(value = "SELECT f FROM Flight f WHERE UPPER(f.departure.name) like  UPPER(concat('%', ?1, '%')) " +
            "AND UPPER(f.arrival.name) like  UPPER(concat('%', ?2, '%'))")
    List<Flight> findByDepartureAndArrivalName(String departureName,String arrivalName);

    @Query(value = "SELECT f FROM Flight f WHERE UPPER(f.departure.city) like  UPPER(concat('%', ?1, '%')) " +
            "AND UPPER(f.arrival.city) like  UPPER(concat('%', ?2, '%'))")
    List<Flight> findByDepartureAndArrivalCity(String departureCity, String arrivalCity);

    @Query(value = "SELECT f FROM Flight f WHERE " +
            "UPPER(f.departure.city) like  UPPER(concat('%', ?1, '%')) or  UPPER(f.departure.name) like  UPPER(concat('%', ?1, '%'))" +
            "AND UPPER(f.arrival.city) like  UPPER(concat('%', ?2, '%')) or UPPER(f.arrival.name) like  UPPER(concat('%', ?2, '%'))")
    List<Flight> findByDepartureAndArrivalCityOrName(String departureCity, String arrivalCity);
}
