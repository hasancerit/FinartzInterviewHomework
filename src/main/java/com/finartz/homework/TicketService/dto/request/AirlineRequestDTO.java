package com.finartz.homework.TicketService.dto.request;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class AirlineRequestDTO {

    @Size(max = 30,message = "name cannot be longer than 30 characters.")
    @NotBlank(message = "name cannot be null.")
    private String name;

    @Size(max = 30,message = "desc cannot be longer than 30 characters.")
    private String desc;

}
