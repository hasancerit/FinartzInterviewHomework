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
    @Size(max = 30,message = "name cannot be longer than 30 characters.")
    @NotBlank(message = "name cannot be null.")
    private String name;

    @Size(max = 30,message = "desc cannot be longer than 30 characters.")
    private String desc;

    @Size(max = 20,message = "city cannot be longer than 20 characters.")
    @NotBlank(message = "name cannot be null.")
    private String city; //Maple

}
