package com.finartz.homework.TicketService.exception.exception;

import com.finartz.homework.TicketService.dto.request.AirportRequestDTO;
import lombok.Data;

@Data
public class CustomAlreadyTaken extends Exception {
    private Class object;
    private String message;
    private String field;
    private String value;


    public CustomAlreadyTaken(String message, Class object, String field, String value) {
        this.field = field;
        this.object = object;
        this.value = value;
        this.message = message;
    }
}
