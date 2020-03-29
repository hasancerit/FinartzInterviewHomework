package com.finartz.homework.TicketService.exception.exception;

import lombok.Data;

import javax.naming.ldap.PagedResultsControl;
import javax.persistence.PrimaryKeyJoinColumn;
import java.time.LocalDateTime;

@Data
public class ArrivalBeforeDepartureException extends Exception {
    private LocalDateTime arrivalDate;
    private LocalDateTime departureDate;
    public ArrivalBeforeDepartureException(LocalDateTime arrivalDate, LocalDateTime departureDate) {
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
    }
}
