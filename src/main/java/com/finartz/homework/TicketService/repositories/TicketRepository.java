package com.finartz.homework.TicketService.repositories;

import com.finartz.homework.TicketService.domain.Flight;
import com.finartz.homework.TicketService.domain.Ticket;
import com.finartz.homework.TicketService.util.FlightClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket,String> {
    Ticket findByPnr(String tickeNo);
    Ticket findByFlightAndNoAndFlightClass(Flight flight, String no, FlightClass flightClass);
}
