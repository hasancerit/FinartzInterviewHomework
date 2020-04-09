package com.finartz.homework.TicketService.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.finartz.homework.TicketService.util.Gender;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;

/*Passanger veritabanında tutulmayacak, Bilet içinde yolcu bilgilerinin görünmesi icin olusturuldu*/
@Getter
@Setter
@Embeddable
public class Passanger {
    @ApiModelProperty(example = "Hasan Cerit",required = true, position = 1)
    @Size(max = 30,message = "fullName cannot be longer than 30 characters.")
    @NotBlank(message = "fullName cannot be null.")
    private String fullName;

    @ApiModelProperty(example = "21697847128",required = true, position = 2)
    @Size(max = 11, min = 11,message = "idenityNo must be 11 characters.") //
    @Pattern(regexp="[0-9]+",message = "idenityNo must have only numbers.")
    private String idenityNo;

    @ApiModelProperty(notes = "It must be MALE or FEMALE",example = "MALE",required = true, position = 3)
    @NotNull(message = "gender cannot be null.")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ApiModelProperty(example = "5077348983",required = true, position = 4)
    @Size(max = 12, min = 10,message = "phoneNumber must be 10-12 characters.")
    @Pattern(regexp="[0-9]+",message = "phoneNumber must have only numbers.")
    private String phoneNumber;

    @ApiModelProperty(example = "10.06.1998",required = true, position = 1)
    @NotNull(message = "dateOfBirth cannot be null.")
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate dateOfBirth;
}
