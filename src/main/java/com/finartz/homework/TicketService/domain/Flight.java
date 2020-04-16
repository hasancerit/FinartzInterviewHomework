package com.finartz.homework.TicketService.domain;


import com.finartz.homework.TicketService.domain.embeddable.Seat;
import com.finartz.homework.TicketService.util.FlightClass;
import com.finartz.homework.TicketService.util.SeatStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Entity
public class Flight implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Airline airline;

    @ManyToOne(fetch = FetchType.LAZY)
    private Airport departure;
    @ManyToOne(fetch = FetchType.LAZY)
    private Airport arrival;

    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;
    private String duration;

    private Double priceEconomy;
    private Double priceBusiness;

    private int capasityBusiness;
    private int capasityEconomy;

    @ElementCollection
    @CollectionTable(name = "flight_seats_business", joinColumns = {@JoinColumn(name = "flight_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "seat_no")
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Map<String, Seat> seatsBusiness = new HashMap<>();


    @ElementCollection
    @CollectionTable(name = "flight_seats_economy", joinColumns = {@JoinColumn(name = "flight_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "seat_no")
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Map<String, Seat> seatsEconomy = new HashMap<>();

    public void setSeatsEmpty(){
        for(int i = 1 ; i <= capasityEconomy ; i++){
            seatsEconomy.put(""+i,new Seat(SeatStatus.empty,null));
        }
        for(int i = 1 ; i <= capasityBusiness ; i++){
            seatsBusiness.put(""+i,new Seat(SeatStatus.empty,null));
        }
    }

    private boolean isFullBusiness = false;
    private boolean isFullEconomy = false;

    public boolean isFullByFlightClass(FlightClass flightClass){
        if(flightClass == FlightClass.BUSINESS)
            return isFullBusiness;
        else
            return isFullEconomy;
    }

    public int getCapasityByFlightClass(FlightClass flightClass){
        if(flightClass == FlightClass.BUSINESS)
            return capasityBusiness;
        else
            return capasityEconomy;
    }

    public Map<String, Seat> getSeatsByFlightClass(FlightClass flightClass){
        if(flightClass == FlightClass.BUSINESS)
            return seatsBusiness;
        else
            return seatsEconomy;
    }

    public Double getPriceByFlightClass(FlightClass flightClass){
        if(flightClass == FlightClass.BUSINESS)
            return priceBusiness;
        else
            return priceEconomy;
    }
}