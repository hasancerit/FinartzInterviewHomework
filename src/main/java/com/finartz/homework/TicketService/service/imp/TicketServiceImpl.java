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
import com.finartz.homework.TicketService.domain.embeddable.Seat;
import com.finartz.homework.TicketService.util.FlightClass;
import com.finartz.homework.TicketService.util.SeatStatus;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final FlightRepository flightRepository;
    private final ModelMapper modelMapper;

    /**
     * Ekleme
     *
     * @param ticketDto         Kaydedilecek Ticket'ın modeli
     * @return                  Kaydedilen Ticket'ın modeli
     * @throws ApiException     Ticket Name zaten varsa
     */
    @Override
    public TicketResponseDTO saveTicket(TicketRequestDTO ticketDto) throws ApiException {
        Ticket ticket = modelMapper.map(ticketDto, Ticket.class);
        return modelMapper.map(handleSaveTicket(ticket, ticketDto), TicketResponseDTO.class);
    }

    /**
     * Güncelleme
     *
     * @param id            Guncellenecek Ticket id'si
     * @param ticketDto     Guncellenecek Ticket'ın yeni alanları
     * @return              Guncellenen Ticket'ın modeli
     * @throws ApiException Ticket id bulunamazsa
     */
    @Override
    public TicketResponseDTO updateTicket(String id, TicketRequestDTO ticketDto) throws ApiException {
        Ticket ticket = null;
        try {
            ticket = ticketRepository.findById(id).get();
        } catch (NoSuchElementException ex) {   //TicketId bulunamazsa
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
        if (oldClass == FlightClass.BUSINESS) {     /*Business ise*/
            oldFlight.getSeatsBusiness().replace(oldNo, new Seat(SeatStatus.empty, null));
            setPriceByFullness(oldFlight,FlightClass.BUSINESS);
        } else {                                    /*Economy ise*/
            oldFlight.getSeatsEconomic().replace(oldNo, new Seat(SeatStatus.empty, null));
            setPriceByFullness(oldFlight,FlightClass.ECONOMI);
        }

        TicketResponseDTO responseDTO = modelMapper.map(handleSaveTicket(ticket, ticketDto), TicketResponseDTO.class);

        flightRepository.save(oldFlight);
        return responseDTO;
    }

    /**
     * Ekleme/ Guncelleme islemini hata kontrolu ile yapar.
     *
     * @param ticket            Kaydedilecek Ticket'in modeli
     * @param ticketDto         Hata durumunda anlamli mesaj döndurmek icin kullanilacak.
     * @return                  Kaydedilen Ticket'in modeli
     * @throws ApiException     Ticketın ait oldugu flightId yoksa,
     *                          Business,Economi dolu ise,
     *                          Kapasite asildi ise,
     *                          Koltuk daha ıonce alindi ise
     */
    private Ticket handleSaveTicket(Ticket ticket, TicketRequestDTO ticketDto) throws ApiException {
        try {
            ticket.setFlight(flightRepository.findById(ticketDto.getFlightId()).get());
        } catch (NoSuchElementException ex) {   //FlightId yoksa hata firlat
            throw new ApiException("Id Not Found", ticketDto.getClass(), "flightId", ticketDto.getFlightId());
        }

        Flight flight = ticket.getFlight();                 //Ticketın flight'i
        String seatNo = ticket.getNo();                     //Ticketın koltuk nosu
        FlightClass flightClass = ticket.getFlightClass();  //Ticketın classı

        if (flightClass == FlightClass.BUSINESS) {  //Business ise
            if(flight.isFullBusiness()){    //Flight dolu mu?
                throw new ApiException("Business İs Full", ticketDto.getClass(), "capasity", "Capasity:"+flight.getCapasityBusiness());
            }

            int capasityBusiness = flight.getSeatsBusiness().size();
            if (Integer.parseInt(seatNo) > capasityBusiness) {  //Sinir asildi ise
                throw new ApiException("Business capacity exceeded", ticketDto.getClass(), "no", ticketDto.getNo());
            }

            SeatStatus status = flight.getSeatsBusiness().get(seatNo).getSeatStatus();  //Alinan koltugun statusu
            if (status != SeatStatus.empty) {                                           //Alınan Koltuk bos degil ise hata firlat
                throw new ApiException("Seat is already taken.", ticketDto.getClass(), "no", ticketDto.getNo());
            }
            flight.getSeatsBusiness().replace(seatNo, new Seat(SeatStatus.taken, ticket));//Bileti Al
        }

        else if (flightClass == FlightClass.ECONOMI) {  //Ekonomi ise
            if(flight.isFullEconomy()){     //Flight dolu mu?
                throw new ApiException("Economy İs Full", ticketDto.getClass(), "capasity", "Capasity:"+flight.getCapasityEconomic());
            }
            int capasityEconomy = flight.getSeatsEconomic().size();
            if (Integer.parseInt(seatNo) > capasityEconomy) {  //Sinir asildi ise
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

    /**
     * Ucusun kapasitesine gore, price belirler. (Kapasite her %10 arttiginda, Price %10 artar.)
     * Ayni zamanda business ya da economy kapasitesi doldu ise full yapar.
     *
     * @param flight        Islem yapilacak flight.
     * @param flightClass   Islem yapilacak flightClass (Business or Economy)
     */
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

        double percentFullnessPrev = (double) (fullnes-1) / capacity;   //Bir onceki doluluk yuzdesi
        double percentFullness = (double) fullnes / capacity;           //Doluluk yuzdesi

        //0.0, 0.1, 0.2, 0.3 vs yuvarlama
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

        if(flightClass == FlightClass.ECONOMI)                          //Değerleri set et
            flight.setPriceEconomic((Math.round(increasedPrice * 100.0)/ 100.0));
        else
            flight.setPriceBusiness((Math.round(increasedPrice * 100.0)/ 100.0));
    }

    /**
     * Silme
     *
     * @param id            Silinecek Ticket id'si
     * @throws ApiException Airline Ticket bulunamazsa
     */
    @Override
    public void deleteTicket(String id) throws ApiException {
        Ticket ticket;
        try {
            ticket = ticketRepository.findById(id).get();
        } catch (NoSuchElementException ex) {   //ticketId yoksa
            throw new ApiException("ticketId Not Found", id.getClass(), "ticketId", id);
        }
        ticketRepository.delete(ticket);
    }


    /**
     * Hepsini cek
     *
     * @return Veritabanındaki tüm Ticket'lar.
     */
    @Override
    public List<TicketResponseDTO> getAll() {
        List<TicketResponseDTO> ticketResponseDTOList = new ArrayList<>();
        ticketRepository.findAll().stream().forEach(ticket -> {     //Her bir ticket
            ticketResponseDTOList.add(modelMapper.map(ticket, TicketResponseDTO.class));    //listeye ekle(dönüstürerek)
        });
        return null;
    }

    /**
     * Id İle Arama
     *
     * @param id    Alinmak istenen Ticket id'si
     * @return      Istenen Tikcet modeli - Id bulunmaz ise null döner.
     */
    @Override
    public TicketResponseDTO getTicket(String id) {
        try {
            return modelMapper.map(ticketRepository.findById(id).get(), TicketResponseDTO.class);
        } catch (NoSuchElementException ex) {   //ticketId yoksa
            return null;
        }
    }

    /**
     * TicketNo'ya göre bul
     *
     * @param ticketNo   Alinmak istenen Ticket id'si
     * @return           Istenen Tikcet modeli - Id bulunmaz ise null döner.
     */
    @Override
    public TicketResponseDTO getTickeyByTicketNo(String ticketNo) {
        return modelMapper.map(ticketRepository.findByTicketNo(ticketNo), TicketResponseDTO.class);
    }
}
