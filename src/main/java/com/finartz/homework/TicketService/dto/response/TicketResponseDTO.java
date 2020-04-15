package com.finartz.homework.TicketService.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.finartz.homework.TicketService.domain.embeddable.Passanger;
import com.finartz.homework.TicketService.util.FlightClass;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(description="Sample Ticket Model for Responses")
public class TicketResponseDTO {
    @ApiModelProperty(position = 1)
    private String id;

    @ApiModelProperty(position = 2)
    private String no;

    @ApiModelProperty(position = 3)
    private FlightClass flightClass;

    @ApiModelProperty(position = 4)
    private String pnr;

    @ApiModelProperty(position = 5)
    private Passanger passanger;

    @ApiModelProperty(position = 6)
    @JsonIgnoreProperties({"seatsEconomic","seatsBusiness"})
    private FlightResponseDTO flight;
}
