package com.finartz.homework.TicketService.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(description="Sample Airline Model for the Post/Update Requests.")
public class AirlineRequestDTO {

    @ApiModelProperty(example = "Türk Hava Yolları", required = true, position = 1)
    @Size(max = 50,message = "name cannot be longer than 50 characters.")
    @NotBlank(message = "name cannot be null.")
    private String name;

    @ApiModelProperty(notes = "Optional Description for Airline",required = false, position = 2)
    @Size(max = 100,message = "desc cannot be longer than 100 characters.")
    private String desc;

    public AirlineRequestDTO(
            @Size(max = 30, message = "name cannot be longer than 30 characters.") @NotBlank(message = "name cannot be null.") String name) {
        this.name = name;
    }
}
