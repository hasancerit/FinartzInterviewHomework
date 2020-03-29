package com.finartz.homework.TicketService.exception.exception;

import lombok.Data;

@Data
public class AlreadTakenSeat extends Exception {
    private String flightId;
    private String no;

    public AlreadTakenSeat(String flightId, String no) {
        this.flightId = flightId;
        this.no = no;
    }
}
