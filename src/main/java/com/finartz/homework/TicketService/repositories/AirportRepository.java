package com.finartz.homework.TicketService.repositories;

import com.finartz.homework.TicketService.domain.Airline;
import com.finartz.homework.TicketService.domain.Airport;
import com.finartz.homework.TicketService.dto.response.AirportResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AirportRepository extends JpaRepository<Airport,String> {
    List<Airport> findByNameIsContainingIgnoreCase(String name);
    List<Airport> findByCityIsContainingIgnoreCase(String city);

    @Query(value = "SELECT a FROM Airport a WHERE UPPER(a.name) like  UPPER(concat('%', ?1, '%')) " +
            "OR UPPER(a.city) like  UPPER(concat('%', ?2, '%'))")
    List<Airport> findByNameOrCity(String name,String city);
}
