package com.finartz.homework.TicketService.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(description="Sample Airport Model for the Post/Update Requests.")
public class AirportRequestDTO {
    @ApiModelProperty(example = "Sabiha Gökçen Havaalanı", required = true, position = 1)
    @Size(max = 30,message = "name cannot be longer than 30 characters.")
    @NotBlank(message = "name cannot be null.")
    private String name;

    @ApiModelProperty(example = "İstanbul", required = true, position = 2)
    @Size(max = 20,message = "city cannot be longer than 20 characters.")
    @NotBlank(message = "name cannot be null.")
    private String city; //Maple

    @ApiModelProperty(notes = "Optional Description for Airport",required = false, position = 3)
    @Size(max = 30,message = "desc cannot be longer than 30 characters.")
    private String desc;

    public AirportRequestDTO(
            @Size(max = 30, message = "name cannot be longer than 30 characters.") @NotBlank(message = "name cannot be null.") String name,
            @Size(max = 20, message = "city cannot be longer than 20 characters.") @NotBlank(message = "name cannot be null.") String city) {
        this.name = name;
        this.city = city;
    }
}
