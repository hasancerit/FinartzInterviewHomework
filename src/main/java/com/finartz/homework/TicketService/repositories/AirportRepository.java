package com.finartz.homework.TicketService.repositories;

import com.finartz.homework.TicketService.domain.Airline;
import com.finartz.homework.TicketService.domain.Airport;
import com.finartz.homework.TicketService.dto.response.AirportResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AirportRepository extends JpaRepository<Airport,String> {
    List<Airport> findByNameIsContainingIgnoreCase(String name);
    List<Airport> findByCityIsContainingIgnoreCase(String city);
    List<Airport> findByNameIgnoreCaseIsContainingOrCityIgnoreCaseIsContaining(String name,String city);
}
