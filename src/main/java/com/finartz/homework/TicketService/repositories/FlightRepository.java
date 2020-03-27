package com.finartz.homework.TicketService.repositories;

import com.finartz.homework.TicketService.domain.Airline;
import com.finartz.homework.TicketService.domain.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlightRepository extends JpaRepository<Flight,String> {
}
