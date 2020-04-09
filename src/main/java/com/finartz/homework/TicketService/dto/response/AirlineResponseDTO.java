package com.finartz.homework.TicketService.dto.response;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.finartz.homework.TicketService.domain.Flight;
import com.finartz.homework.TicketService.util.FlightClass;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(description="Sample Airline Model for Responses")
public class AirlineResponseDTO {
    @ApiModelProperty(position = 1)
    private String id;
    @ApiModelProperty(position = 2)
    private String name;
    @ApiModelProperty(position = 3)
    private String desc;
    @ApiModelProperty(position = 4,notes = "Active Flights of the airline.")
    @JsonIgnoreProperties("airline")
    private List<FlightResponseDTO> activeFlights = new ArrayList<>();
}
