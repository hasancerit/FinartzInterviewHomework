package com.finartz.homework.TicketService.dto.response.wrapper;

import com.finartz.homework.TicketService.dto.response.FlightResponseDTO;
import lombok.Data;

import java.util.List;

@Data
public class FlightsResponseDTO {
    private List<FlightResponseDTO> directFlights;
    private List<IndirectFlightDTO> indirectFlights;
}
