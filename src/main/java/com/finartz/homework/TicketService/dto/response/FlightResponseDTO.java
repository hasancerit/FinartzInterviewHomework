package com.finartz.homework.TicketService.dto.response;

import com.fasterxml.jackson.annotation.*;
import com.finartz.homework.TicketService.dto.response.wrapper.SeatDetail;
import com.finartz.homework.TicketService.dto.response.wrapper.SeatsStatus;
import com.finartz.homework.TicketService.domain.embeddable.Seat;
import com.finartz.homework.TicketService.util.SeatStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Map;

@NoArgsConstructor
@Data
@ApiModel(description="Sample Flight Model for Responses")
public class FlightResponseDTO {
    @ApiModelProperty(position = 1)
    private String id;

    @ApiModelProperty(position = 2)
    @JsonIgnoreProperties("activeFlights") //Sonsuz donguyu engellemek icin
    private AirlineResponseDTO airline;

    @ApiModelProperty(position = 3,notes = "Departure Airport of the Flight.")
    @JsonIgnoreProperties({"departureFlights","arrivalFlights"})
    private AirportResponseDTO departure;

    @ApiModelProperty(position = 4,notes = "Arrival Airport of the Flight.")
    @JsonIgnoreProperties({"departureFlights","arrivalFlights"})
    private AirportResponseDTO arrival;

    @ApiModelProperty(position = 5)
    @DateTimeFormat
    @JsonFormat(pattern="dd-MM-yyyy HH:mm")
    private LocalDateTime departureDate;

    @ApiModelProperty(position = 6)
    @JsonFormat(pattern="dd-MM-yyyy HH:mm")
    private LocalDateTime arrivalDate;

    @ApiModelProperty(position = 7)
    private String duration;

    @ApiModelProperty(position = 8)
    private Double priceEconomic;
    @ApiModelProperty(position = 9)
    private Double priceBusiness;

    @ApiModelProperty(position = 10)
    private int capasityBusiness;
    @ApiModelProperty(position = 11)
    private int capasityEconomic;

    /*Bunlar veritabanından direkt gelen koltuklar, ekonomik ve business ayrı ayrı fakat dolu-boş koltuklar karışık.
    * Yani, seatsEconomic icinde, Ekonomi sınıfının boş ve dolu koltukları ortak, farklı farklı değil. Business için de öyle.
    *
    * Bu yüzden kullanıcıya koltuklar, hem ekonomi hem de business için dolu ve boş koltukların ayrı ayrı gösterilecek bir
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
                seatStatusEconomic.getTakenSeats().add(new SeatDetail(k,v.getTicket().getId()));
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
                seatStatusBusiness.getTakenSeats().add(new SeatDetail(k,v.getTicket().getId()));
        });
        setSeatStatusBusiness(seatStatusBusiness);
    }


    /*Bu nesneler ekonomi ve business icin boş-dolu koltukları ayrı ayrı tutacak. Kullanıcıya bunlar gideecek*/
    @ApiModelProperty(position = 12,notes = "Keeps Empty and Taken Seats in the Economy Class Separately.")
    private SeatsStatus seatStatusEconomi;
    @ApiModelProperty(position = 13,notes = "Keeps Empty and Taken Seats in the Business Class Separately.")
    private SeatsStatus seatStatusBusiness;
}
