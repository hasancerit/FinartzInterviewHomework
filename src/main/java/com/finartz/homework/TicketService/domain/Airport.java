package com.finartz.homework.TicketService.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Airport{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(unique = true)
    private String name;
    private String city; //Maple
    private String desc;

    @OneToMany(mappedBy = "departure")
    private List<Flight> departureFlights = new ArrayList<>();
}
