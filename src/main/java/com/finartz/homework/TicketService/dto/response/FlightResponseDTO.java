package com.finartz.homework.TicketService.dto.response;

import com.fasterxml.jackson.annotation.*;
import com.finartz.homework.TicketService.dto.response.wrapper.SeatDetail;
import com.finartz.homework.TicketService.dto.response.wrapper.SeatsStatus;
import com.finartz.homework.TicketService.domain.embeddable.Seat;
import com.finartz.homework.TicketService.util.SeatStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.mapping.Collection;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
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
    private Double priceEconomy;
    @ApiModelProperty(position = 9)
    private Double priceBusiness;

    @ApiModelProperty(position = 10)
    private int capasityBusiness;
    @ApiModelProperty(position = 11)
    private int capasityEconomy;

    /*Bunlar veritabanından direkt gelen koltuklar, ekonomi ve business ayrı ayrı fakat dolu-boş koltuklar karisik.
    * Yani, seatsEconomy icinde, Ekonomi sınıfının boş ve dolu koltukları ortak, farklı farklı değil. Business için de öyle.
    *
    * Bu yüzden kullanıcıya koltuklar, hem ekonomi hem de business için dolu ve boş koltukların ayrı ayrı gösterilecek bir
    * formda gönderilmek isteniyor.
    *
    * Bunun icin kullaniciya bunlari degil, alttaki seatStatusEconomy,seatStatusBusiness wrapperlarını,
    * ayarlayarak(seatsEconomy ve seatsBusiness set edildiğinde, wrapper objeleri de set et) gönderilecek.
    * */
    @JsonIgnore
    private Map<String, Seat> seatsEconomy;
    @JsonIgnore
    private Map<String, Seat> seatsBusiness;

    public void setSeatsEconomy(Map<String, Seat> seatsEconomy){
        this.seatsEconomy = seatsEconomy;

        /*Economy için dolu ve boş koltukları, kullanıcıya gidecek wrapper nesneye ayrı ayrı ekle*/
        SeatsStatus tempSeatStatusEconomy = new SeatsStatus();
        seatsEconomy.forEach((k,v)->{
            if(v.getSeatStatus() == SeatStatus.empty)
                tempSeatStatusEconomy.getEmptySeats().add(k);
            else
                tempSeatStatusEconomy.getTakenSeats().add(new SeatDetail(k,v.getTicket().getId()));
        });

        tempSeatStatusEconomy.sort();
        setSeatStatusEconomy(tempSeatStatusEconomy);
    }

    public void setSeatsBusiness(Map<String, Seat> seatsBusiness){
        this.seatsBusiness = seatsBusiness;

        /*Business için dolu ve boş koltukları, kullanıcıya gidecek wrapper nesneye ayrı ayrı ekle*/
        SeatsStatus seatStatusBusinessTemp = new SeatsStatus();
        seatsBusiness.forEach((k,v)->{
            if(v.getSeatStatus() == SeatStatus.empty)
                seatStatusBusinessTemp.getEmptySeats().add(""+k);
            else
                seatStatusBusinessTemp.getTakenSeats().add(new SeatDetail(""+k,v.getTicket().getId()));
        });

        seatStatusBusinessTemp.sort();
        setSeatStatusBusiness(seatStatusBusinessTemp);
    }


    /*Bu nesneler ekonomi ve business icin boş-dolu koltukları ayrı ayrı tutacak. Kullanıcıya bunlar gideecek*/
    @ApiModelProperty(position = 12,notes = "Keeps Empty and Taken Seats in the Economy Class Separately.")
    private SeatsStatus seatStatusEconomy;
    @ApiModelProperty(position = 13,notes = "Keeps Empty and Taken Seats in the Business Class Separately.")
    private SeatsStatus seatStatusBusiness;


    /*Getters Setters*/
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AirlineResponseDTO getAirline() {
        return airline;
    }

    public void setAirline(AirlineResponseDTO airline) {
        this.airline = airline;
    }

    public AirportResponseDTO getDeparture() {
        return departure;
    }

    public void setDeparture(AirportResponseDTO departure) {
        this.departure = departure;
    }

    public AirportResponseDTO getArrival() {
        return arrival;
    }

    public void setArrival(AirportResponseDTO arrival) {
        this.arrival = arrival;
    }

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDateTime departureDate) {
        this.departureDate = departureDate;
    }

    public LocalDateTime getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDateTime arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Double getPriceEconomy() {
        return priceEconomy;
    }

    public void setPriceEconomy(Double priceEconomy) {
        this.priceEconomy = priceEconomy;
    }

    public Double getPriceBusiness() {
        return priceBusiness;
    }

    public void setPriceBusiness(Double priceBusiness) {
        this.priceBusiness = priceBusiness;
    }

    public int getCapasityBusiness() {
        return capasityBusiness;
    }

    public void setCapasityBusiness(int capasityBusiness) {
        this.capasityBusiness = capasityBusiness;
    }

    public int getCapasityEconomy(){
        return capasityEconomy;
    }

    public void setCapasityEconomy(int capasityEconomy) {
        this.capasityEconomy = capasityEconomy;
    }

    public Map<String, Seat> getSeatsEconomy() {
        return seatsEconomy;
    }

    public Map<String, Seat> getSeatsBusiness() {
        return seatsBusiness;
    }

    public SeatsStatus getSeatStatusEconomy() {
        return seatStatusEconomy;
    }

    public void setSeatStatusEconomy(SeatsStatus seatStatusEconomy) {
        this.seatStatusEconomy = seatStatusEconomy;
    }

    public SeatsStatus getSeatStatusBusiness() {
        return seatStatusBusiness;
    }

    public void setSeatStatusBusiness(SeatsStatus seatStatusBusiness) {
        this.seatStatusBusiness = seatStatusBusiness;
    }
}
