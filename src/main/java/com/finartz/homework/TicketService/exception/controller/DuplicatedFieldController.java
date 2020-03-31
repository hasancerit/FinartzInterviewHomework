package com.finartz.homework.TicketService.exception.controller;

import com.finartz.homework.TicketService.exception.exception.ApiException;
import com.finartz.homework.TicketService.exception.exception.ArrivalBeforeDepartureException;
import com.finartz.homework.TicketService.exception.message.ApiError;
import com.finartz.homework.TicketService.exception.message.ApiValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class DuplicatedFieldController {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handleConflict(ApiException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage("Api Error");
        apiError.setHttpStatus(HttpStatus.BAD_REQUEST);

        ApiValidationError apiValidationError = new ApiValidationError(ex.getMessage(),ex.getObject().getSimpleName(),
                                                                    ex.getField(),ex.getValue());
        apiError.getSubErrors().add(apiValidationError);

        return new ResponseEntity<>(apiError,apiError.getHttpStatus());
    }

    @ExceptionHandler(ArrivalBeforeDepartureException.class)
    public ResponseEntity<ApiError> handleConflict(ArrivalBeforeDepartureException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage("Arrival Date("+ex.getArrivalDate()+"), cannot be before from Departure Date("+ex.getDepartureDate()+")");
        apiError.setHttpStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiError,apiError.getHttpStatus());
    }


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
