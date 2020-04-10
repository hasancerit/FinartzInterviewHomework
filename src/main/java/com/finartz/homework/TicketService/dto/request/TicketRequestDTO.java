package com.finartz.homework.TicketService.dto.request;

import com.finartz.homework.TicketService.domain.embeddable.Passanger;
import com.finartz.homework.TicketService.util.FlightClass;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(description="Sample Ticket Model for the Post/Update Requests.")
public class TicketRequestDTO {
    @ApiModelProperty(example = "2",required = true, position = 1)
    @NotBlank(message = "flightId cannot be null.")
    private String flightId;

    @ApiModelProperty(notes = "Flight Class of Ticket.",example = "BUSINESS",required = true, position = 2)
    @NotNull(message = "flightClass cannot be null.")
    private FlightClass flightClass;

    @ApiModelProperty(required = true, position = 3)
    @Valid
    @NotNull(message = "passanger cannot be null.")
    Passanger passanger;

    @ApiModelProperty(example = "42",required = true, position = 4)
    @NotBlank(message = "no cannot be null.")
    @Pattern(regexp="[0-9]+",message = "no must have only numbers.")
    private String no;
}
