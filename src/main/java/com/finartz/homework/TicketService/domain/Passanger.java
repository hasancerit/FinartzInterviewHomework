package com.finartz.homework.TicketService.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.finartz.homework.TicketService.util.Gender;
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
    @Size(max = 30,message = "fullName cannot be longer than 30 characters.")
    @NotBlank(message = "fullName cannot be null.")
    private String fullName;

    @Size(max = 11, min = 11,message = "idenityNo must be 11 characters.") //
    @Pattern(regexp="[0-9]+",message = "idenityNo must have only numbers.")
    private String idenityNo;

    @NotNull(message = "gender cannot be null.")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Size(max = 12, min = 10,message = "phoneNumber must be 10-12 characters.")
        @Pattern(regexp="[0-9]+",message = "phoneNumber must have only numbers.")
    private String phoneNumber;

    @NotNull(message = "dateOfBirth cannot be null.")
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate dateOfBirth;
}
