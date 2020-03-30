package com.finartz.homework.TicketService.domain;


import com.finartz.homework.TicketService.util.SeatStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Double priceEconomic;
    private Double priceBusiness;

    private int capasityBusiness;
    private int capasityEconomic;

    @ElementCollection
    @CollectionTable(name = "flight_seats_buniness", joinColumns = {@JoinColumn(name = "flight_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "seat_no")
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Map<String, SeatStatus> seatsBusiness = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "flight_seats_economic", joinColumns = {@JoinColumn(name = "flight_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "seat_no")
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Map<String, SeatStatus> seatsEconomic = new HashMap<>();

    public void setSeatsEmpty(){
        for(int i = 1 ; i <= capasityEconomic ; i++){
            seatsEconomic.put(Integer.toString(i),SeatStatus.empty);
        }
        for(int i = 1 ; i <= capasityBusiness ; i++){
            seatsBusiness.put(Integer.toString(i),SeatStatus.empty);
        }
    }
}