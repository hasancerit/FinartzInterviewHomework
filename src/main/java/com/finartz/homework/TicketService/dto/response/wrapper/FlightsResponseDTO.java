package com.finartz.homework.TicketService.dto.response.wrapper;

import com.finartz.homework.TicketService.dto.response.FlightResponseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class FlightsResponseDTO {
    @ApiModelProperty(position = 1,notes = "Direct Flight List")
    private List<FlightResponseDTO> directFlights;

    @ApiModelProperty(position = 1,notes = "Indirect Flight List")
    private List<IndirectFlightDTO> indirectFlights;
}
