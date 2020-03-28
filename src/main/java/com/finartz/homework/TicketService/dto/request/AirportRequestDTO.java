package com.finartz.homework.TicketService.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class AirportRequestDTO {
    @Size(max = 30)
    @NotBlank
    private String name;

    @Size(max = 30)
    private String desc;

    @Size(max = 20)
    @NotBlank
    private String city; //Maple

}
