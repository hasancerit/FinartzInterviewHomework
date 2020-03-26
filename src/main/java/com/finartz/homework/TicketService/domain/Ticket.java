package com.finartz.homework.TicketService.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Ticket{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String ticketNo;
    @ManyToOne
    private User user;
    @ManyToOne
    private Flight flight;
    private String flightClass;
    private String no;
}
