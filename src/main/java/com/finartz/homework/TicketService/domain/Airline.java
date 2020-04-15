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
public class Airline{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(unique = true)
    private String name;
    private String desc;

    @OneToMany(mappedBy="airline",cascade = {CascadeType.REMOVE})
    private List<Flight> activeFlights = new ArrayList<>();

    public Airline(String id,String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }
}
