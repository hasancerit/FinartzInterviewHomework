package com.finartz.homework.TicketService.exception.controller;

import com.finartz.homework.TicketService.exception.exception.CustomAlreadyTaken;
import com.finartz.homework.TicketService.exception.exception.CustomNotFound;
import com.finartz.homework.TicketService.exception.message.ApiError;
import com.finartz.homework.TicketService.exception.message.ApiSubError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ErrorController {
    @ExceptionHandler(CustomAlreadyTaken.class)
    public ResponseEntity<ApiError> handleConflict(CustomAlreadyTaken ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage("Api Error");
        apiError.setHttpStatus(HttpStatus.BAD_REQUEST);

        ApiSubError apiSubError = new ApiSubError(
                ex.getMessage(),
                ex.getObject().getSimpleName(),
                ex.getField(),
                ex.getValue());
        apiError.getSubErrors().add(apiSubError);

        return new ResponseEntity<>(apiError,apiError.getHttpStatus());
    }

    @ExceptionHandler(CustomNotFound.class)
    public ResponseEntity<ApiError> handleNotFound(CustomNotFound ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage("Api Error - NOT FOUND");   //Genel Mesaj
        apiError.setHttpStatus(HttpStatus.NOT_FOUND);   //Status

        String message =ex.getField() +  " Not Found";
        ApiSubError apiSubError = new ApiSubError(
                message,                        //Mesaj
                ex.getObject().getSimpleName(), //Modeli
                ex.getField(),                  //Alani
                ex.getValue());                 //Degeri

        apiError.getSubErrors().add(apiSubError);

        return new ResponseEntity<>(apiError,apiError.getHttpStatus());
    }


    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        ApiError apiError = new ApiError();
        apiError.setHttpStatus(HttpStatus.BAD_REQUEST);
        apiError.setMessage("Validation Error");

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            ApiSubError subError = new ApiSubError(fieldError.getDefaultMessage(),fieldError.getObjectName(),
                    fieldError.getField(),fieldError.getRejectedValue());
            apiError.getSubErrors().add(subError);
        }
        return new ResponseEntity<>(apiError,apiError.getHttpStatus());
    }
}
