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
    @Size(max = 30,message = "fullName 30 karakterden büyük olamaz")
    @NotBlank(message = "fullName Capacity Id Bos Birakilamaz.")
    private String fullName;

    @Size(max = 11, min = 11,message = "idenityNo 11 Hane Olmalı") //!
    private String idenityNo;

    @NotNull(message = "gender Capacity Id Bos Birakilamaz.")
    private Gender gender;

    @Size(max = 12, min = 10,message = "phoneNumber 10-12 Hane Olmalı")
    private String phoneNumber;

    @NotNull(message = "dateOfBirth Capacity Id Bos Birakilamaz.")
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate dateOfBirth;
}
