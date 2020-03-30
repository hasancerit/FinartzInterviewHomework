package com.finartz.homework.TicketService.dto.response.wrapper;

import com.finartz.homework.TicketService.dto.response.FlightResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@AllArgsConstructor
@Data
public class IndirectFlightDTO {
    private FlightResponseDTO firstFlight;
    private FlightResponseDTO secondFlight;
}
