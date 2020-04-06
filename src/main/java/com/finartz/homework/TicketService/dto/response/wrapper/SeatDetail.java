package com.finartz.homework.TicketService.dto.response.wrapper;

import com.finartz.homework.TicketService.dto.response.TicketResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SeatDetail {
    private String no;
    private TicketResponseDTO ticketResponseDTO;
}
