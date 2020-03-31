package com.finartz.homework.TicketService.exception.controller;

import com.finartz.homework.TicketService.exception.message.ApiError;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class EnumDateController {
    /**Enum Search Parameters**/
    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<ApiError> handleConflict(ConversionFailedException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage("Invalid Search Parameter:"+ex.getValue());
        apiError.setHttpStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiError,apiError.getHttpStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleConflict(HttpMessageNotReadableException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getLocalizedMessage());
        apiError.setHttpStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiError,apiError.getHttpStatus());
    }
}
