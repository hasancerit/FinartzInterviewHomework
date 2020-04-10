package com.finartz.homework.TicketService.domain.embeddable;

import com.finartz.homework.TicketService.domain.Ticket;
import com.finartz.homework.TicketService.util.FlightClass;
import com.finartz.homework.TicketService.util.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Data
public class Seat{
    @Enumerated(EnumType.STRING)
    private SeatStatus seatStatus;
    @OneToOne
    private Ticket ticket;
}
