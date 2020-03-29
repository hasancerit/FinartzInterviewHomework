package com.finartz.homework.TicketService.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class FlightRequestDTO {
    @NotBlank(message = "Airline Id Bos Birakilamaz.")
    private String airlineId;
    @NotBlank(message = "Departure Id Bos Birakilamaz.")
    private String departureAirportId;
    @NotBlank(message = "Arrival Id Bos Birakilamaz.")
    private String arrivalAirportId;

    @NotNull(message = "Departure Date Id Bos Birakilamaz.")
    @JsonFormat(pattern="dd-MM-yyyy HH:mm")
    private LocalDateTime departureDate;
    @NotNull(message = "Arrival Date Id Bos Birakilamaz.")
    @JsonFormat(pattern="dd-MM-yyyy HH:mm")
    private LocalDateTime arrivalDate;

    @NotNull(message = "Price Economic Id Bos Birakilamaz.")
    private Double priceEconomic;
    @NotNull(message = "Business Economic Id Bos Birakilamaz.")
    private Double priceBusiness;

    @NotNull(message = "Business Capacity Id Bos Birakilamaz.")
    private int capasityBusiness;
    @NotNull(message = "Economic Capacity Id Bos Birakilamaz.")
    private int capasityEconomic;
}
