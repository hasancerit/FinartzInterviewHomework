package com.finartz.homework.TicketService.dto.request;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class AirlineRequestDTO {

    @Size(max = 30,message = "Name 30 karakterden büyük olamaz")
    @NotBlank(message = "Name Bos Birakilamaz")
    private String name;

    @Size(max = 30,message = "Description 30 karakterden büyük olamaz")
    private String desc;

}
