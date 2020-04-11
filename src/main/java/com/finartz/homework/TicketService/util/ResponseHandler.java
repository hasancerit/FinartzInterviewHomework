package com.finartz.homework.TicketService.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {
    public static ResponseEntity<?> createResponse(Object object){
        ResponseEntity<Object> responseEntity;
        if(object == null){
            responseEntity =  new ResponseEntity<>(object, HttpStatus.NOT_FOUND);
        }
        else {
            responseEntity = new ResponseEntity<>(object, HttpStatus.OK);
        }
        return responseEntity;
    }
}
