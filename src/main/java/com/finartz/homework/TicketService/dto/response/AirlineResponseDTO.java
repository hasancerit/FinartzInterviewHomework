package com.finartz.homework.TicketService.dto.response;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.finartz.homework.TicketService.domain.Flight;
import com.finartz.homework.TicketService.util.FlightClass;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AirlineResponseDTO {
    private String id;
    private String name;
    private String desc;
    @JsonIgnoreProperties("airline")
    private List<FlightResponseDTO> activeFlights = new ArrayList<>();
}
