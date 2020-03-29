package com.finartz.homework.TicketService.exception.controller;

import com.finartz.homework.TicketService.exception.exception.AlreadTakenSeat;
import com.finartz.homework.TicketService.exception.message.ApiError;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TicketErrorController {
    @ExceptionHandler(AlreadTakenSeat.class)
    public ResponseEntity<ApiError> handleConflict(AlreadTakenSeat ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getNo()+" Numaralı Koltuk Zaten Alinmis."+"Flight Id:"+ex.getFlightId());
        apiError.setHttpStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiError,apiError.getHttpStatus());
    }
}
