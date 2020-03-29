package com.finartz.homework.TicketService.exception.exception;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TakenSeatException extends Exception {
    private List<String> emptySeats = new ArrayList<>();
    private String message;

    public TakenSeatException(List<String> emptySeats,String message){
        this.emptySeats = emptySeats;
        this.message = message;
    }
}
