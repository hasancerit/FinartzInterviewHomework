package com.finartz.homework.TicketService.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class TicketRequestDTO {
    private String userId;

    private String flightId;

    private String flightClass;
    private String no;
}
