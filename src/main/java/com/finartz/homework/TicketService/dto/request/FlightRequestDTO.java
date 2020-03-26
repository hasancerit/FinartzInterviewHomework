package com.finartz.homework.TicketService.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
public class FlightRequestDTO {
    private String airlineId;

    private String departureAirportId;
    private String arrivalAirportId;
    /*
    private Date departureDate;
    private Date arrivalDate;
    */
    private String duration;

    private Double priceEconomic;
    private Double priceBusiness;

    private int capasityBusiness;
    private int capasityEconomic;
}
