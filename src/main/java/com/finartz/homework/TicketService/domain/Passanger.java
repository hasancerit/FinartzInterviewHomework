package com.finartz.homework.TicketService.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.finartz.homework.TicketService.util.Gender;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;

/*Passanger veritabanında tutulmayacak, Bilet içinde yolcu bilgilerinin görünmesi icin olusturuldu*/
@Getter
@Setter
@Embeddable
public class Passanger {
    @Size(max = 30)
    @NotBlank
    private String fullName;

    @Size(max = 11, min = 11) //!
    private String idenityNo;

    @NotNull
    private Gender gender;

    @Size(max = 12, min = 10)
    private String phoneNumber;

    @NotNull
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate dateOfBirth;
}
