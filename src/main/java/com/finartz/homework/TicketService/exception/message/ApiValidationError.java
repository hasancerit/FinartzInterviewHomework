package com.finartz.homework.TicketService.exception.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApiValidationError extends SubError{
    private String message;

    private String object;
    private String field;
    private Object rejectedValue;
}

abstract class SubError{ }