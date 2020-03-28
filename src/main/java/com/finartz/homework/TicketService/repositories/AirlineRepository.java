package com.finartz.homework.TicketService.repositories;

import com.finartz.homework.TicketService.domain.Airline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AirlineRepository extends JpaRepository<Airline,String> {
    List<Airline> findByNameIsContainingIgnoreCase(String name);
}
