package com.finartz.homework.TicketService.dto.response;

import com.fasterxml.jackson.annotation.*;
import com.finartz.homework.TicketService.dto.response.wrapper.SeatsStatus;
import com.finartz.homework.TicketService.domain.Seat;
import com.finartz.homework.TicketService.util.SeatStatus;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class FlightResponseDTO {
    private String id;

    @JsonIgnoreProperties("activeFlights") //Sonsuz donguyu engellemek icin
    private AirlineResponseDTO airline;

    @JsonIgnoreProperties({"departureFlights","arrivalFlights"})
    private AirportResponseDTO departure;

    @JsonIgnoreProperties({"departureFlights","arrivalFlights"})
    private AirportResponseDTO arrival;


    @DateTimeFormat
    @JsonFormat(pattern="dd-MM-yyyy HH:mm")
    private LocalDateTime departureDate;

    @JsonFormat(pattern="dd-MM-yyyy HH:mm")
    private LocalDateTime arrivalDate;

    private String duration;

    private Double priceEconomic;
    private Double priceBusiness;

    private int capasityBusiness;
    private int capasityEconomic;

    /*Bunlar veritabanından direkt gelen koltuklar, ekonomik ve business ayrı ayrı fakat dolu-boş koltuklar karışık
    * Yani, seatsEconomic icinde, Ekonomi sınıfının boş ve dolu koltukları ortak, farklı farklı değil. Business için de öyle.
    *
    * Bu yüzden kullanıcıya, hem ekonomi hem de business için dolu ve boş koltukların ayrı ayrı gösterilecek bir
    * formda gönderilmek isteniyor.
    *
    * Bunun için kullanıcıya bunları değil, alttaki seatStatusEconomi,seatStatusBusiness wrapperlarını,
    * ayarlayarak(seatsEconomic ve seatsBusiness set edildiğinde, wrapper objeleri de set et) gönderilecek.
    * */
    @JsonIgnore
    private Map<String, Seat> seatsEconomic;
    @JsonIgnore
    private Map<String, Seat> seatsBusiness;

    public void setSeatsEconomic(Map<String, Seat> seatsEconomic){
        this.seatsEconomic = seatsEconomic;

        /*Economi için dolu ve boş koltukları, kullanıcıya gidecek wrapper nesneye ayrı ayrı ekle*/
        SeatsStatus seatStatusEconomic = new SeatsStatus();
        seatsEconomic.forEach((k,v)->{
            if(v.getSeatStatus() == SeatStatus.empty)
                seatStatusEconomic.getEmptySeats().add(k);
            else
                seatStatusEconomic.getTakenSeats().add(k);
        });
        setSeatStatusEconomi(seatStatusEconomic);
    }
    public void setSeatsBusiness(Map<String, Seat> seatsBusiness){
        this.seatsBusiness = seatsBusiness;
        /*Business için dolu ve boş koltukları, kullanıcıya gidecek wrapper nesneye ayrı ayrı ekle*/
        SeatsStatus seatStatusBusiness = new SeatsStatus();
        seatsBusiness.forEach((k,v)->{
            if(v.getSeatStatus() == SeatStatus.empty)
                seatStatusBusiness.getEmptySeats().add(k);
            else
                seatStatusBusiness.getTakenSeats().add(k);
        });
        setSeatStatusBusiness(seatStatusBusiness);
    }


    /*Bu nesneler ekonomi ve business icin boş-dolu koltukları ayrı ayrı tutacak. Kullanıcıya bunlar gideecek*/
    private SeatsStatus seatStatusEconomi;
    private SeatsStatus seatStatusBusiness;

}
