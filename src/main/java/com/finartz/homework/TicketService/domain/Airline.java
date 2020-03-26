package com.finartz.homework.TicketService.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Airline{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String name;
    private String desc;

    @OneToMany(mappedBy="airline")
    private List<Flight> activeFlights = new ArrayList<>();
}
