package com.finartz.homework.TicketService.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.finartz.homework.TicketService.domain.Flight;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AirportResponseDTO {
    private String id;
    private String name;
    private String city; //Maple
    private String desc;
    @JsonIgnoreProperties({"departure","arrival"})
    private List<FlightResponseDTO> departureFlights = new ArrayList<>();
    @JsonIgnoreProperties({"departure","arrival"})
    private List<FlightResponseDTO> arrivalFlights = new ArrayList<>();
}
