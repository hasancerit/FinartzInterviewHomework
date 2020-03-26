package com.finartz.homework.TicketService.repositories;

import com.finartz.homework.TicketService.domain.Airline;
import com.finartz.homework.TicketService.domain.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportRepository extends JpaRepository<Airport,String> {
}
