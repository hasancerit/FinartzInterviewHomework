package com.finartz.homework.TicketService.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class AirportRequestDTO {
    private String name;
    private String city; //Maple
    private String desc;
}
