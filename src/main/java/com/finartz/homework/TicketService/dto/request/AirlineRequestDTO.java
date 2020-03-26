package com.finartz.homework.TicketService.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.*;

@Data
public class AirlineRequestDTO {

    private String name;
    private String desc;

}
