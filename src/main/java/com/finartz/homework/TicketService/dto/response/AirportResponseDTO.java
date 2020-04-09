package com.finartz.homework.TicketService.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.finartz.homework.TicketService.domain.Flight;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(description="Sample Airport Model for Responses")
public class AirportResponseDTO {
    @ApiModelProperty(position = 1)
    private String id;
    @ApiModelProperty(position = 2)
    private String name;
    @ApiModelProperty(position = 3)
    private String city;
    @ApiModelProperty(position = 4)
    private String desc;
    @ApiModelProperty(position = 5,notes = "Flights to depart from this airport.")
    @JsonIgnoreProperties({"departure","arrival"})
    private List<FlightResponseDTO> departureFlights = new ArrayList<>();
    @ApiModelProperty(position = 6,notes = "Flights to arrival to this airport.")
    @JsonIgnoreProperties({"departure","arrival"})
    private List<FlightResponseDTO> arrivalFlights = new ArrayList<>();
}
