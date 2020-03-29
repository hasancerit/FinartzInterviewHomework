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
    @Size(max = 30,message = "Name 30 karakterden büyük olamaz")
    @NotBlank(message = "Name Bos Birakilamaz")
    private String name;

    @Size(max = 30,message = "Description 30 karakterden büyük olamaz")
    private String desc;

    @Size(max = 20,message = "City 20 karakterden büyük olamaz")
    @NotBlank(message = "City Bos Birakilamaz")
    private String city; //Maple

}
