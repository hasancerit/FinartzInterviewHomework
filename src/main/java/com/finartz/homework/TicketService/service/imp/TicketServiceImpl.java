package com.finartz.homework.TicketService.service.imp;

import com.finartz.homework.TicketService.domain.Flight;
import com.finartz.homework.TicketService.domain.Ticket;
import com.finartz.homework.TicketService.dto.request.TicketRequestDTO;
import com.finartz.homework.TicketService.dto.response.FlightResponseDTO;
import com.finartz.homework.TicketService.dto.response.TicketResponseDTO;
import com.finartz.homework.TicketService.exception.exception.ApiException;
import com.finartz.homework.TicketService.repositories.FlightRepository;
import com.finartz.homework.TicketService.repositories.TicketRepository;
import com.finartz.homework.TicketService.service.TicketService;
import com.finartz.homework.TicketService.domain.Seat;
import com.finartz.homework.TicketService.util.FlightClass;
import com.finartz.homework.TicketService.util.SeatStatus;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private ModelMapper modelMapper;

    /*Ekleme*/
    @Override
    public TicketResponseDTO saveTicket(TicketRequestDTO ticketDto) throws ApiException {
        Ticket ticket = modelMapper.map(ticketDto, Ticket.class);
        return modelMapper.map(handleSaveTicket(ticket, ticketDto), TicketResponseDTO.class);
    }

    /*Güncelleme*/
    @Override
    public TicketResponseDTO updateTicket(String id, TicketRequestDTO ticketDto) throws ApiException {
        Ticket ticket = null;
        try {
            ticket = ticketRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new ApiException("ticketId Not Found", id.getClass(), "ticketId", id);
        }

        //Eski koltugu kaldirmak icin.
        String oldFlightId = ticket.getFlight().getId();
        FlightClass oldClass = ticket.getFlightClass();
        String oldNo = ticket.getNo();

        //Yeni ticketı güncelle
        ticket.setPassanger(ticketDto.getPassanger());
        ticket.setFlightClass(ticketDto.getFlightClass());
        ticket.setNo(ticketDto.getNo());
        ticket.setFlight(flightRepository.getOne(ticketDto.getFlightId()));

        //Eski koltugu kaldir.
        Flight oldFlight = flightRepository.getOne(oldFlightId);
        if (oldClass == FlightClass.BUSINESS) {
            oldFlight.getSeatsBusiness().replace(oldNo, new Seat(SeatStatus.empty, null));
        } else {
            oldFlight.getSeatsEconomic().replace(oldNo, new Seat(SeatStatus.empty, null));
        }

        TicketResponseDTO responseDTO = modelMapper.map(handleSaveTicket(ticket, ticketDto), TicketResponseDTO.class);

        flightRepository.save(oldFlight);
        return responseDTO;
    }

    private Ticket handleSaveTicket(Ticket ticket, TicketRequestDTO ticketDto) throws ApiException {
        try {                                                           //Flight Id var mi? Varsa set et yoksa hata firlat
            ticket.setFlight(flightRepository.findById(ticketDto.getFlightId()).get());
        } catch (NoSuchElementException ex) {
            throw new ApiException("Id Not Found", ticketDto.getClass(), "flightId", ticketDto.getFlightId());
        }

        Flight flight = ticket.getFlight();                             //Ticketın flight'ı
        String seatNo = ticket.getNo();                                 //Ticketın koltuk nosu
        FlightClass flightClass = ticket.getFlightClass();              //Ticketın classı

        if (flightClass == FlightClass.BUSINESS) {                                      //Business ise
            if(flight.isFullBusiness()){                                                //Flight dolu mu?
                throw new ApiException("Business İs Full", ticketDto.getClass(), "capasity", "Capasity:"+flight.getCapasityBusiness());
            }

            if (Integer.parseInt(seatNo) > flight.getSeatsBusiness().size()) {          //Sinir asildi ise
                throw new ApiException("Business capacity exceeded", ticketDto.getClass(), "no", ticketDto.getNo());
            }

            SeatStatus status = flight.getSeatsBusiness().get(seatNo).getSeatStatus();  //Alinan koltugun statusu
            if (status != SeatStatus.empty) {                                           //Alınan Koltuk bos degil ise hata firlat
                throw new ApiException("Seat is already taken.", ticketDto.getClass(), "no", ticketDto.getNo());
            }
            flight.getSeatsBusiness().replace(seatNo, new Seat(SeatStatus.taken, ticket));//Bileti Al
        } else if (flightClass == FlightClass.ECONOMI) {                                //Ekonomi ise
            if(flight.isFullEconomy()){                                                //Flight dolu mu?
                throw new ApiException("Economy İs Full", ticketDto.getClass(), "capasity", "Capasity:"+flight.getCapasityEconomic());
            }
            if (Integer.parseInt(seatNo) > flight.getSeatsEconomic().size()) {          //Sinir asildi ise
                throw new ApiException("Business capacity exceeded", ticketDto.getClass(), "no", ticketDto.getNo());
            }
            SeatStatus status = flight.getSeatsEconomic().get(seatNo).getSeatStatus();  //Alinan koltugun statusu
            if (status != SeatStatus.empty) {                                           //Alınan Koltuk boş değil ise hata firlat
                throw new ApiException("Seat is already taken.", ticketDto.getClass(), "no", ticketDto.getNo());
            }
            flight.getSeatsEconomic().replace(seatNo, new Seat(SeatStatus.taken, ticket));//Bileti Al
        }
        setPriceByFullness(flight,flightClass); //ucusun doluluk oranina göre fiyatını belirle. isFull'u set et.

        ticketRepository.save(ticket);
        flightRepository.save(flight);
        return ticket;
    }

    private void setPriceByFullness(Flight flight,FlightClass flightClass) {
        int fullnes;            //kac kisi koltuk almis
        int capacity;           //Toplam kapasite
        Double lastPrice;       //Son ucret
        if(flightClass == FlightClass.ECONOMI){
            fullnes = modelMapper.map(flight, FlightResponseDTO.class).getSeatStatusEconomi().getTakenSeats().size();
            capacity = flight.getCapasityEconomic();
            lastPrice = flight.getPriceEconomic();

            if(capacity == fullnes) flight.setFullEconomy(true);
        }
        else{
            fullnes = modelMapper.map(flight, FlightResponseDTO.class).getSeatStatusBusiness().getTakenSeats().size();
            capacity = flight.getCapasityBusiness();
            lastPrice = flight.getPriceBusiness();

            if(capacity == fullnes) flight.setFullBusiness(true);
        }

        double percentFullnessPrev = (double) (fullnes-1) / capacity;
        double percentFullness = (double) fullnes / capacity;

        percentFullness = Math.floor(percentFullness * 10) / 10;
        percentFullnessPrev = Math.floor(percentFullnessPrev * 10) / 10;

        int countIncreasePrev = (int) (10 * percentFullnessPrev);     //Su ana kadar kac kere %10 zam gelmis
        Double firstPrice = lastPrice;                                //Son Ucret
        for (int i = 0; i < countIncreasePrev; i++) {
            firstPrice = firstPrice * (10.0 / 11.0);                  //İlk ücreti bul(Bu yöntemin kullanılma sebebi,
                                                                      //                      update'lerde de kullanmak)
        }

        int countIncrease = countIncreasePrev;                         //Suan kac kere %10 zam gelecek
        if((percentFullness * 10) > countIncreasePrev)                 //Eger onceki ucak, onceki yolcuya göre seviye atladi ise artir
            countIncrease += 1;

        Double increasedPrice = firstPrice;                             //Artirilacak ucret
        for (int i = 0; i < countIncrease; i++) {
            increasedPrice += (increasedPrice * 0.1);                   //Ilk ucreti zamli olan ucrete dondur.
        }

        if(flightClass == FlightClass.ECONOMI)                          //Değerleri setEt
            flight.setPriceEconomic((Math.round(increasedPrice * 100.0)/ 100.0));
        else
            flight.setPriceBusiness((Math.round(increasedPrice * 100.0)/ 100.0));
    }

    /*Silme*/
    @Override
    public void deleteTicket(String id) throws ApiException {
        Ticket ticket;
        try {
            ticket = ticketRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new ApiException("ticketId Not Found", id.getClass(), "ticketId", id);
        }
        ticketRepository.delete(ticket);
    }


    /*Hepsini Bul*/
    @Override
    public List<TicketResponseDTO> getAll() {
        List<TicketResponseDTO> ticketResponseDTOList = new ArrayList<>();
        ticketRepository.findAll().stream().forEach(ticket -> {
            ticketResponseDTOList.add(modelMapper.map(ticket, TicketResponseDTO.class));
        });
        return null;
    }

    /*Id'ye göre Bul*/
    @Override
    public TicketResponseDTO getTicket(String id) {
        try {
            return modelMapper.map(ticketRepository.findById(id).get(), TicketResponseDTO.class);
        } catch (NoSuchElementException ex) {
            return null;
        }
    }

    /*TicketNo'ya göre bul*/
    @Override
    public TicketResponseDTO getTickeyByTicketNo(String ticketNo) {
        return modelMapper.map(ticketRepository.findByTicketNo(ticketNo), TicketResponseDTO.class);
    }


}
