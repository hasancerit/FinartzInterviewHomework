package com.finartz.homework.TicketService.dto.response.wrapper;

import com.finartz.homework.TicketService.util.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SeatsStatus {
    private List<String> takenSeats = new ArrayList<>();
    private List<String> emptySeats = new ArrayList<>();
}
