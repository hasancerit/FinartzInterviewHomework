package com.finartz.homework.TicketService.dto.response;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
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


    @DateTimeFormat
    @JsonFormat(pattern="dd-MM-yyyy HH:mm")
    private LocalDateTime departureDate;

    @JsonFormat(pattern="dd-MM-yyyy HH:mm")
    private LocalDateTime arrivalDate;


    private String duration;

    private Double priceEconomic;
    private Double priceBusiness;

    private int capasityBusiness;
    private int capasityEconomic;

    private List<String> takenSeatsEconomi;
    private List<String> takenSeatsBusiness;


}
