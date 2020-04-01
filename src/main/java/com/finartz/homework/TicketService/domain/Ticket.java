package com.finartz.homework.TicketService.domain;

import com.finartz.homework.TicketService.util.FlightClass;
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

    @Column(unique = true)
    private String ticketNo;
    @Embedded
    private Passanger passanger;
    @ManyToOne
    private Flight flight;

    @Enumerated(EnumType.STRING)
    private FlightClass flightClass;
    private String no;
}
