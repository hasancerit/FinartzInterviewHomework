package com.finartz.homework.TicketService.exception.exception;

import lombok.Data;

@Data
public class CustomNotFound extends Exception {
    private Class object;
    private String field;
    private String value;


    public CustomNotFound(Class object, String field, String value) {
        this.field = field;
        this.object = object;
        this.value = value;
    }
}
