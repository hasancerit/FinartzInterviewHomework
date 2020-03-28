package com.finartz.homework.TicketService.dto.request;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class AirlineRequestDTO {

    @Size(max = 30)
    @NotBlank
    private String name;

    @Size(max = 30)
    private String desc;

}
