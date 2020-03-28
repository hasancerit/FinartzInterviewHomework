package com.finartz.homework.TicketService.bootstrap;

import com.finartz.homework.TicketService.domain.Airline;
import com.finartz.homework.TicketService.domain.Airport;
import com.finartz.homework.TicketService.domain.Flight;
import com.finartz.homework.TicketService.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

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

    @Override
    public void run(String... args) throws Exception {
      /*  Airline airline = Airline.builder().name("Pegasus").desc("Şirket").build();
        airlineRepository.save(airline);

        Airport departur = Airport.builder().name("Sabiha Gökçen").desc("Havaalanı").sehir("İstanbul").build();
        Airport arrival = Airport.builder().name("Adnan Menderes").desc("Havaalanı").sehir("İzmir").build();

        airportRepository.save(departur);
        airportRepository.save(arrival);

        Flight flight = new Flight();
        flight.setAirline(airline);
        flight.setDeparture(departur);
        flight.setArrival(arrival);
        flight.setArrivalDate(new Date());
        flight.setDepartureDate(new Date());
        flight.setCapasityBusiness(100);
        flight.setCapasityEconomic(300);
        flight.setDuration("2H 20M");
        flight.setEmptyChairsBusiness(Arrays.asList("2","2","3","4","5","6","7","8"));
        flight.setEmptyChairsEconomi(Arrays.asList("1","2","3","4","5","6","7"));
        flight.setPriceBusiness(500.0);
        flight.setPriceEconomic(300.5);

        flightRepository.save(flight);

        Flight flight2 = new Flight();
        flight2.setAirline(airline);
        flight2.setDeparture(departur);
        flight2.setArrival(arrival);
        flight2.setArrivalDate(new Date());
        flight2.setDepartureDate(new Date());
        flight2.setCapasityBusiness(100);
        flight2.setCapasityEconomic(300);
        flight2.setDuration("3H 20M");
        flight2.setEmptyChairsBusiness(Arrays.asList("4","6","3","4","5","6","7","8"));
        flight2.setEmptyChairsEconomi(Arrays.asList("1","2","3","4","5","6","7"));
        flight2.setPriceBusiness(500.0);
        flight2.setPriceEconomic(300.5);

        flightRepository.save(flight2);*/
    }
}
