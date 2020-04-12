package com.finartz.homework.TicketService.exception.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiSubError extends SubError{
    private String message;

    private String object;
    private String field;
    private Object rejectedValue;
}

abstract class SubError{ }