package com.finartz.homework.TicketService.dto.response;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.finartz.homework.TicketService.domain.Airline;
import com.finartz.homework.TicketService.domain.Airport;
import lombok.Data;

import java.util.List;

@Data
public class FlightResponseDTO {
    private String id;

    @JsonIgnoreProperties("activeFlights") //Sonsuz donguyu engellemek icin
    private AirlineResponseDTO airline;

    @JsonIgnoreProperties({"departureFlights","arrivalFlights"})
    private AirportResponseDTO departure;

    @JsonIgnoreProperties({"departureFlights","arrivalFlights"})
    private AirportResponseDTO arrival;

    /*
    private Date departureDate;
    private Date arrivalDate;
    */

    private String duration;

    private Double priceEconomic;
    private Double priceBusiness;

    private int capasityBusiness;
    private int capasityEconomic;

    private List<String> takenSeatsEconomi;
    private List<String> takenSeatsBusiness;


}
