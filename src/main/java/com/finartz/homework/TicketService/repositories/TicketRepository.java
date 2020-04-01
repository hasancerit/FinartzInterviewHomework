package com.finartz.homework.TicketService.repositories;

import com.finartz.homework.TicketService.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket,String> {
    Ticket findByTicketNo(String tickeNo);
}
