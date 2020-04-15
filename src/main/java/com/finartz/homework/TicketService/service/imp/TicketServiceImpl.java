package com.finartz.homework.TicketService.service.imp;

import com.finartz.homework.TicketService.domain.Flight;
import com.finartz.homework.TicketService.domain.Ticket;
import com.finartz.homework.TicketService.dto.request.TicketRequestDTO;
import com.finartz.homework.TicketService.dto.response.FlightResponseDTO;
import com.finartz.homework.TicketService.dto.response.TicketResponseDTO;
import com.finartz.homework.TicketService.exception.exception.CustomAlreadyTaken;
import com.finartz.homework.TicketService.exception.exception.CustomNotFound;
import com.finartz.homework.TicketService.repositories.FlightRepository;
import com.finartz.homework.TicketService.repositories.TicketRepository;
import com.finartz.homework.TicketService.service.TicketService;
import com.finartz.homework.TicketService.domain.embeddable.Seat;
import com.finartz.homework.TicketService.util.FlightClass;
import com.finartz.homework.TicketService.util.SeatStatus;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
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
     * @param ticketReqDto    Kaydedilecek Ticket'ın modeli
     * @return                Kaydedilen Ticket'ın modeli
     * @throws CustomAlreadyTaken   Business,Economi dolu ise
     *                              Kapasite asildi ise,
     *                              Koltuk daha once alindi ise
     * @throws CustomNotFound       flightId bulunamazsa
     * @see #handleSaveTicket(Ticket, TicketRequestDTO)
     */
    @Override
    public TicketResponseDTO saveTicket(TicketRequestDTO ticketReqDto) throws CustomAlreadyTaken, CustomNotFound {
        Ticket ticket = modelMapper.map(ticketReqDto, Ticket.class);
        ticket = handleSaveTicket(ticket, ticketReqDto);

        return modelMapper.map(ticket, TicketResponseDTO.class);
    }


    /**
     * Güncelleme
     *
     * @param id              Guncellenecek Ticket id'si
     * @param ticketReqDto    Guncellenecek Ticket'ın yeni alanları
     * @return                Guncellenen Ticket'ın modeli
     * @throws CustomAlreadyTaken   Business,Economi dolu ise,(Validation ile)
     *                              Kapasite asildi ise,      (Validation ile)
     *                              Koltuk daha once alindi ise
     * @throws CustomNotFound       ticketId bulunamazsa
     *                              flightId bulunamazsa
     * @see #handleSaveTicket(Ticket, TicketRequestDTO)
     */
    @Override
    public TicketResponseDTO updateTicket(String id, TicketRequestDTO ticketReqDto) throws CustomAlreadyTaken, CustomNotFound {
        Ticket ticket = null;
        try {
            ticket = ticketRepository.findById(id).get();
        } catch (NoSuchElementException ex) {   //TicketId bulunamazsa
            throw new CustomNotFound(id.getClass(), "ticketId", id);
        }


        //Eski koltugu kaldir (Yer degistirmedi ise, handleSave kisminda zaten yeniden kaydedilecek)
        Flight oldFlight = ticket.getFlight();
        FlightClass oldClass = ticket.getFlightClass();
        String oldNo = ticket.getNo();

        oldFlight.getSeatsByFlightClass(oldClass).replace(oldNo, new Seat(SeatStatus.empty, null));
        setPriceByFullness(oldFlight, oldClass,false);

        //Yeni ticketı güncelle
        ticket.setPassanger(ticketReqDto.getPassanger());
        ticket.setFlightClass(ticketReqDto.getFlightClass());
        ticket.setNo(ticketReqDto.getNo());

        //Bileti Almayi Dene
        Ticket savedTicket = handleSaveTicket(ticket, ticketReqDto);
        TicketResponseDTO responseDTO = modelMapper.map(savedTicket, TicketResponseDTO.class);

        //Ucak degismesi durumunda, eski ucagi guncelle
        //Ucak degismezse, handleSaveTicket metodunda zaten guncelleniyor
        if(!oldFlight.getId().equals(responseDTO.getFlight().getId()))
            flightRepository.save(oldFlight);
        return responseDTO;
    }


    /**
     * Ekleme/ Guncelleme islemini hata kontrolu ile yapar.
     *
     * @param ticket            Kaydedilecek Ticket'in modeli
     * @param ticketReqDto      Hata durumunda anlamli mesaj döndurmek icin kullanilacak.
     * @return                  Kaydedilen Ticket'in modeli
     * @throws CustomNotFound   Ticketın ait oldugu flightId yoksa
     * @throws CustomAlreadyTaken     Business,Economi dolu ise,(Validation ile)
     *                          Kapasite asildi ise,      (Validation ile)
     *                          Koltuk daha once alindi ise
     */
    private Ticket handleSaveTicket(Ticket ticket, TicketRequestDTO ticketReqDto) throws CustomAlreadyTaken, CustomNotFound {
        try {
            Flight flight = flightRepository.findById(ticketReqDto.getFlightId()).get();
            ticket.setFlight(flight);
        } catch (NoSuchElementException ex) {   //FlightId yoksa hata firlat
            throw new CustomNotFound(
                    ticketReqDto.getClass(),
                    "flightId",
                    ticketReqDto.getFlightId());
        }

        Flight flight = ticket.getFlight();                 //Ticketın flight'i
        String seatNo = ticket.getNo();                     //Ticketın koltuk nosu
        FlightClass flightClass = ticket.getFlightClass();  //Ticketın classı

        if(flight.isFullByFlightClass(flightClass)){
            throw new CustomAlreadyTaken(
                    flightClass.toString() + " İs Full",
                    ticketReqDto.getClass(), "capasity",
                    "Capasity:"+flight.getCapasityBusiness());
        }


        int capasity = flight.getCapasityByFlightClass(flightClass);
        if (Integer.parseInt(seatNo) > capasity) {  //Sinir asildi ise
            throw new CustomAlreadyTaken(
                    flightClass.toString() + " capacity exceeded",
                    ticketReqDto.getClass(),
                    "no",
                    ticketReqDto.getNo());
        }

        SeatStatus status = flight.getSeatsByFlightClass(flightClass).get(seatNo).getSeatStatus();
        if (status != SeatStatus.empty) {                                           //Alınan Koltuk bos degil ise hata firlat
            throw new CustomAlreadyTaken(
                    "Seat is already taken.",
                    ticketReqDto.getClass(),
                    "no",
                    ticketReqDto.getNo());
        }

        ticket = ticketRepository.save(ticket);//Bileti Al

        flight.getSeatsByFlightClass(flightClass).replace(seatNo, new Seat(SeatStatus.taken, ticket));//Ucusun koltuklarini guncelle
        setPriceByFullness(flight,flightClass,true); //ucusun doluluk oranina göre fiyatını belirle. isFull'u set et.
        flightRepository.save(flight);

        return ticket;
    }


    /**
     * Ucusun kapasitesine gore, price belirler. (Kapasite her %10 arttiginda, Price %10 artar.)
     * Ayni zamanda business ya da economy kapasitesi doldu ise full yapar.
     *
     * @param flight        Islem yapilacak flight.
     * @param flightClass   Islem yapilacak flightClass (Business or Economy)
     * @param isSave        true ise kayit islemi, false ise iptal islemi
     */
    private void setPriceByFullness(Flight flight,FlightClass flightClass,boolean isSave) {
        int fullnes;            //kac kisi koltuk almis
        int capacity;           //Toplam kapasite

        Double lastPrice;       //Son ucret
        capacity = flight.getCapasityByFlightClass(flightClass);
        lastPrice = flight.getPriceByFlightClass(flightClass);

        if(flightClass == FlightClass.ECONOMY){
            fullnes = modelMapper.map(flight, FlightResponseDTO.class).getSeatStatusEconomi().getTakenSeats().size();
            if(capacity == fullnes) flight.setFullEconomy(true);
        }
        else{
            fullnes = modelMapper.map(flight, FlightResponseDTO.class).getSeatStatusBusiness().getTakenSeats().size();
            if(capacity == fullnes) flight.setFullBusiness(true);
        }

        double percentFullnessPrev;                                     //Bir onceki doluluk yuzdesi
        if(isSave) percentFullnessPrev = (double) (fullnes-1) / capacity;
        else percentFullnessPrev = (double) (fullnes+1) / capacity;

        double percentFullness = (double) fullnes / capacity;           //Doluluk yuzdesi

        //0.0, 0.1, 0.2, 0.3 vs yuvarlama
        percentFullness = Math.floor(percentFullness * 10) / 10;
        percentFullnessPrev = Math.floor(percentFullnessPrev * 10) / 10;

        int countIncreasePrev = (int) (10 * percentFullnessPrev);     //Su ana kadar kac kere %10 zam gelmis
        Double firstPrice = lastPrice;                                //Son Ucret
        for (int i = 0; i < countIncreasePrev; i++) {
            firstPrice = firstPrice * (10.0 / 11.0);                  //İlk ücreti bul(Bu yöntemin kullanılma sebebi,
                                                                      //                      iptallerde de kullanmak)
        }

        int countIncrease = countIncreasePrev;                         //Suan kac kere %10 zam gelecek
        if((percentFullness * 10) > countIncreasePrev)                 //Eger ucak, onceki yolcuya göre seviye atladi ise artir
           countIncrease += 1;
        if((percentFullness * 10) < countIncreasePrev)                 //                               seviye düstü ise azalt
            countIncrease -= 1;

        Double increasedPrice = firstPrice;                             //Artirilacak ucret
        for (int i = 0; i < countIncrease; i++) {
            increasedPrice += (increasedPrice * 0.1);                   //Ilk ucreti zamli olan ucrete dondur.
        }

        if(flightClass == FlightClass.ECONOMY)                          //Değerleri set et
            flight.setPriceEconomic((Math.round(increasedPrice * 100.0)/ 100.0));
        else
            flight.setPriceBusiness((Math.round(increasedPrice * 100.0)/ 100.0));
    }


    /**
     * Silme
     *
     * @param id                Silinecek Ticket id'si
     * @throws CustomNotFound   ticketId bulunamazsa
     */
    @Override
    public void deleteTicket(String id) throws CustomNotFound {
        try {
            ticketRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {   //ticketId yoksa
            throw new CustomNotFound(id.getClass(), "ticketId", id);
        }
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
        return ticketResponseDTOList;
    }


    /**
     * Id İle Arama
     *
     * @param id                Alinmak istenen Ticket id'si
     * @return                  Istenen Tikcet modeli
     * @throws CustomNotFound   ticketId bulunamazsa
     */
    @Override
    public TicketResponseDTO getTicket(String id) throws CustomNotFound {
        try {
            Ticket ticket = ticketRepository.findById(id).get();
            return modelMapper.map(ticket, TicketResponseDTO.class);
        } catch (NoSuchElementException ex) {   //ticketId yoksa
            throw new CustomNotFound(id.getClass(), "ticketId", id);
        }
    }


    /**
     * TicketNo'ya göre bul
     *
     * @param ticketNo          Alinmak istenen Ticket id'si
     * @return                  Istenen Tikcet modeli
     * @throws CustomNotFound   ticketId bulunamazsa
     */
    @Override
    public TicketResponseDTO getTickeyByPnr(String pnr) throws CustomNotFound {
        try {
            return modelMapper.map(ticketRepository.findByTicketNo(pnr), TicketResponseDTO.class);
        } catch (NoSuchElementException ex) {   //ticketId yoksa
            throw new CustomNotFound(pnr.getClass(), "pnr", pnr);
        }
    }
}
