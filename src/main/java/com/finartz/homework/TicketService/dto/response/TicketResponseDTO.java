package com.finartz.homework.TicketService.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.finartz.homework.TicketService.domain.Passanger;
import com.finartz.homework.TicketService.util.FlightClass;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel(description="Sample Ticket Model for Responses")
public class TicketResponseDTO {
    @ApiModelProperty(position = 1)
    private String id;
    @ApiModelProperty(position = 2)
    private String ticketNo;

    private Passanger passanger;

    @ApiModelProperty(position = 3)
    @JsonIgnoreProperties({"seatsEconomic","seatsBusiness"})
    private FlightResponseDTO flight;

    @ApiModelProperty(position = 4)
    private FlightClass flightClass;

    @ApiModelProperty(position = 5)
    private String no;
}
