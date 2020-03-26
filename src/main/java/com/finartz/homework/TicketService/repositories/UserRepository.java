package com.finartz.homework.TicketService.repositories;

import com.finartz.homework.TicketService.domain.Ticket;
import com.finartz.homework.TicketService.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
}
