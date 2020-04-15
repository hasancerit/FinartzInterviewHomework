package com.finartz.homework.TicketService.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
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

    @OneToMany(mappedBy = "departure",cascade = {CascadeType.REMOVE})
    private List<Flight> departureFlights = new ArrayList<>();

    @OneToMany(mappedBy = "arrival",cascade = {CascadeType.REMOVE})
    private List<Flight> arrivalFlights = new ArrayList<>();

    public Airport(String id,String name, String city, String desc) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.desc = desc;
    }
}
