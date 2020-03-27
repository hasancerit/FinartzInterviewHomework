package com.finartz.homework.TicketService.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class FlightsResponseDTO {
    private List<FlightResponseDTO> aktarmasizUcuslar;
    private List<FlightResponseDTO> aktarmaliUcuslar;
}
