package com.finartz.homework.TicketService.exception.controller;

import com.finartz.homework.TicketService.exception.message.ApiError;
import com.finartz.homework.TicketService.exception.message.ApiValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ValidationController {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiError> handleUserNotFound(MethodArgumentNotValidException ex) {
        ApiError apiError = new ApiError();
        apiError.setHttpStatus(HttpStatus.BAD_REQUEST);
        apiError.setMessage("Validation Error");

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            ApiValidationError subError = new ApiValidationError(fieldError.getDefaultMessage(),fieldError.getObjectName(),
                                                                    fieldError.getField(),fieldError.getRejectedValue());
            apiError.getSubErrors().add(subError);
        }
        return new ResponseEntity<>(apiError,apiError.getHttpStatus());
    }
}