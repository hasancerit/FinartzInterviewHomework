package com.finartz.homework.TicketService.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Flight{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ManyToOne
    private Airline airline;

    @ManyToOne
    private Airport departure;
    @ManyToOne
    private Airport arrival;

    private Date departureDate;
    private Date arrivalDate;
    private String duration;

    private Double priceEconomic;
    private Double priceBusiness;

    private int capasityBusiness;
    private int capasityEconomic;

    @ElementCollection
    private List<String> emptyChairsBusiness;

    @ElementCollection
    private List<String> emptyChairsEconomi;
}