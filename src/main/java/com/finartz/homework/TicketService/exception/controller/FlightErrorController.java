package com.finartz.homework.TicketService.exception.controller;

import com.finartz.homework.TicketService.exception.exception.ArrivalBeforeDepartureException;
import com.finartz.homework.TicketService.exception.message.ApiError;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FlightErrorController {
    @ExceptionHandler(ArrivalBeforeDepartureException.class)
    public ResponseEntity<ApiError> handleConflict(ArrivalBeforeDepartureException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage("Arrival Date("+ex.getArrivalDate()+"), Departure Date("+ex.getDepartureDate()+")'den önce olamaz.");
        apiError.setHttpStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiError,apiError.getHttpStatus());
    }
}
