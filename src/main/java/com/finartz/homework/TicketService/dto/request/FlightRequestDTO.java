package com.finartz.homework.TicketService.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@ApiModel(description="Sample Flight Model for the Post/Update Requests.")
public class FlightRequestDTO {

    @ApiModelProperty(example = "23", required = true, position = 1)
    @NotBlank(message = "airlineId cannot be null.")
    private String airlineId;

    @ApiModelProperty(example = "12", required = true, position = 2)
    @NotBlank(message = "departureAirportId cannot be null.")
    private String departureAirportId;

    @ApiModelProperty(example = "56", required = true, position = 3)
    @NotBlank(message = "arrivalAirportId cannot be null.")
    private String arrivalAirportId;

    @ApiModelProperty(example = "11.12.2020 19:30", required = true, position = 4)
    @NotNull(message = "departureDate cannot be null.")
    @JsonFormat(pattern="dd-MM-yyyy HH:mm")
    private LocalDateTime departureDate;

    @ApiModelProperty(example = "12.12.2020 08:45", required = true, position = 5)
    @NotNull(message = "arrivalDate cannot be null.")
    @JsonFormat(pattern="dd-MM-yyyy HH:mm")
    private LocalDateTime arrivalDate;

    @ApiModelProperty(example = "200", required = true, position = 6)
    @NotNull(message = "priceEconomic cannot be null.")
    private Double priceEconomic;

    @ApiModelProperty(example = "400", required = true, position = 6)
    @NotNull(message = "priceBusiness cannot be null.")
    private Double priceBusiness;

    @ApiModelProperty(example = "50", required = true, position = 6)
    @NotNull(message = "capasityBusiness cannot be null.")
    private int capasityBusiness;

    @ApiModelProperty(example = "150", required = true, position = 6)
    @NotNull(message = "capasityEconomic cannot be null.")
    private int capasityEconomic;
}
