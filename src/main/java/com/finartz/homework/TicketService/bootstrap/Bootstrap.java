package com.finartz.homework.TicketService.bootstrap;

import com.finartz.homework.TicketService.domain.Airline;
import com.finartz.homework.TicketService.domain.Airport;
import com.finartz.homework.TicketService.domain.Flight;
import com.finartz.homework.TicketService.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class Bootstrap implements CommandLineRunner {
    @Autowired
    AirlineRepository airlineRepository;
    @Autowired
    AirportRepository airportRepository;
    @Autowired
    FlightRepository flightRepository;
    @Autowired
    TicketRepository ticketRepository;

    private void airlines(){

    }

    @Override
    public void run(String... args) throws Exception {
        Airline thy = new Airline();
        thy.setName("THY");
        thy.setDesc("THY Aciklama");
        airlineRepository.save(thy);

        Airline pagasus = new Airline();
        pagasus.setName("Pegasus");
        pagasus.setDesc("Pegasus Aciklama");
        airlineRepository.save(pagasus);

        Airline anadolu = new Airline();
        anadolu.setName("AnadoluJet");
        anadolu.setDesc("AnadoluJet Aciklama");
        airlineRepository.save(anadolu);

        Airline sun = new Airline();
        sun.setName("SunExpress");
        sun.setDesc("SunExpress Aciklama");
        airlineRepository.save(sun);

        airlineRepository.save(thy);
        airlineRepository.save(pagasus);
        airlineRepository.save(anadolu);
        airlineRepository.save(sun);

        airlines();
        Airport airport1 = new Airport();
        airport1.setName("Şakirpaşa Havalimanı");
        airport1.setCity("Adana");
        airportRepository.save(airport1);

        Airport airport2 = new Airport();
        airport2.setName("Esenboğa Havalimanı");
        airport2.setCity("Ankara");
        airportRepository.save(airport2);

        Airport airport3 = new Airport();
        airport3.setName("Antalya Havalimanı");
        airport3.setCity("Antalya");
        airportRepository.save(airport3);

        Airport airport4 = new Airport();
        airport4.setName("Gazipaşa Havalimanı");
        airport4.setCity("Antalya");
        airportRepository.save(airport4);

        Airport airport5 = new Airport();
        airport5.setName("Koca Seyit Havalimanı");
        airport5.setCity("Balıkesir");
        airportRepository.save(airport5);

        Airport airport6 = new Airport();
        airport6.setName("Yenişehir Havalimanı");
        airport6.setCity("Bursa");
        airportRepository.save(airport6);

        Airport airport7 = new Airport();
        airport7.setName("Erkilet Havalimanı");
        airport7.setCity("Kayseri");
        airportRepository.save(airport7);

        Airport airport8 = new Airport();
        airport8.setName("Çanakkale Havalimanı");
        airport8.setCity("Çanakkale");
        airportRepository.save(airport8);

        Airport airport9 = new Airport();
        airport9.setName("Çardak Havalimanı");
        airport9.setCity("Denizli");
        airportRepository.save(airport9);

        Airport airport10 = new Airport();
        airport10.setName("Diyarbakır Havalimanı");
        airport10.setCity("Diyarbakır");
        airportRepository.save(airport10);

        Airport airport11 = new Airport();
        airport11.setName("Dalaman Havalimanı");
        airport11.setCity("Muğla");
        airportRepository.save(airport11);

        Airport airport12 = new Airport();
        airport12.setName("Elazığ Havalimanı");
        airport12.setCity("Elazığ");
        airportRepository.save(airport12);

        Airport airport13 = new Airport();
        airport13.setName("Sabiha Gökçen Havalimanı");
        airport13.setCity("İstanbul");
        airportRepository.save(airport13);

        Airport airport14 = new Airport();
        airport14.setName("Atatürk Havalimanı");
        airport14.setCity("İstanbul");
        airportRepository.save(airport14);

        Airport airport15 = new Airport();
        airport15.setName("Adnan Menderes Havalimanı");
        airport15.setCity("İzmir");
        airportRepository.save(airport15);


        for(int i = 0 ; i < 40; i++){
            Flight flight = new Flight();
            Random random = new Random();

            int rndAirlineId = 1+random.nextInt(4);
            flight.setAirline(airlineRepository.findById(""+rndAirlineId).get());

            Airport air1Response = null;
            Airport air2Response = null;
            do{
                int air1 = 1+random.nextInt(15);
                int air2= 1+random.nextInt(15);

                air1Response = airportRepository.findById(""+air1).get();
                air2Response =  airportRepository.findById(""+air2).get();
            }while(air1Response.getCity().equalsIgnoreCase(air2Response.getCity()));

            flight.setDeparture(air1Response);
            flight.setArrival(air2Response);


            LocalDateTime time1 = null;
            LocalDateTime time2 = null;
            int day = 1+random.nextInt(2);
            do{
                int hour = 1+random.nextInt(18);
                int minute = 1+random.nextInt(59);

                int hour2 = 1+random.nextInt(18);
                int minute2 = 1+random.nextInt(59);

                time1 = LocalDateTime.of(2020,4,day,hour,minute);
                time2 = LocalDateTime.of(2020,4,day,hour2,minute2);
            }while(time2.isBefore(time1));



            flight.setDepartureDate(time1);
            flight.setArrivalDate(time2);
            flight.setCapasityBusiness(10);
            flight.setCapasityEconomy(20);
            flight.setPriceBusiness(500.0);
            flight.setPriceEconomy(170.99);
            flight.setSeatsEmpty();
            String duration = Duration.between(flight.getArrivalDate(), flight.getDepartureDate()).toString();
            flight.setDuration(duration.replace("-", " ").substring(3));
            flightRepository.save(flight);
        }
    }
}
