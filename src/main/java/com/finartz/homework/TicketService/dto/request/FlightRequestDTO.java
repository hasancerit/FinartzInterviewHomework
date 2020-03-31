package com.finartz.homework.TicketService.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
public class FlightRequestDTO {
    @NotBlank(message = "airlineId cannot be null.")
    private String airlineId;
    @NotBlank(message = "departureAirportId cannot be null.")
    private String departureAirportId;
    @NotBlank(message = "arrivalAirportId cannot be null.")
    private String arrivalAirportId;

    @NotNull(message = "departureDate cannot be null.")
    @JsonFormat(pattern="dd-MM-yyyy HH:mm")
    private LocalDateTime departureDate;
    @NotNull(message = "arrivalDate cannot be null.")
    @JsonFormat(pattern="dd-MM-yyyy HH:mm")
    private LocalDateTime arrivalDate;

    @NotNull(message = "priceEconomic cannot be null.")
    private Double priceEconomic;

    @NotNull(message = "priceBusiness cannot be null.")
    private Double priceBusiness;

    @NotNull(message = "capasityBusiness cannot be null.")
    private int capasityBusiness;

    @NotNull(message = "capasityEconomic cannot be null.")
    private int capasityEconomic;
}
