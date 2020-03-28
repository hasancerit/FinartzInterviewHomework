package com.finartz.homework.TicketService.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.finartz.homework.TicketService.util.Gender;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

/*Passanger veritabanında tutulmayacak, Bilet içinde yolcu bilgilerinin görünmesi icin olusturuldu*/
@Getter
@Setter
@Embeddable
public class Passanger {
    private String fullName;
    private String idenityNo;
    private Gender gender;
    private String phoneNumber;
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate dateOfBirth;
}
