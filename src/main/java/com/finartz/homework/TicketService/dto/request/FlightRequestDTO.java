package com.finartz.homework.TicketService.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class FlightRequestDTO {
    @NotBlank
    private String airlineId;
    @NotBlank
    private String departureAirportId;
    @NotBlank
    private String arrivalAirportId;

    @NotNull
    @JsonFormat(pattern="dd-MM-yyyy HH:mm")
    private LocalDateTime departureDate;
    @NotNull
    @JsonFormat(pattern="dd-MM-yyyy HH:mm")
    private LocalDateTime arrivalDate;

    @NotNull
    private Double priceEconomic;
    @NotNull
    private Double priceBusiness;

    @NotNull
    private int capasityBusiness;
    @NotNull
    private int capasityEconomic;
}
