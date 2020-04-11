package com.finartz.homework.TicketService.service.imp;

import com.finartz.homework.TicketService.domain.Airline;
import com.finartz.homework.TicketService.domain.Airport;
import com.finartz.homework.TicketService.domain.Flight;
import com.finartz.homework.TicketService.domain.Ticket;
import com.finartz.homework.TicketService.dto.request.FlightRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirportResponseDTO;
import com.finartz.homework.TicketService.dto.response.wrapper.IndirectFlightDTO;
import com.finartz.homework.TicketService.dto.response.FlightResponseDTO;
import com.finartz.homework.TicketService.dto.response.wrapper.FlightsResponseDTO;
import com.finartz.homework.TicketService.exception.exception.ArrivalBeforeDepartureException;
import com.finartz.homework.TicketService.exception.exception.ApiException;
import com.finartz.homework.TicketService.repositories.AirlineRepository;
import com.finartz.homework.TicketService.repositories.AirportRepository;
import com.finartz.homework.TicketService.repositories.FlightRepository;
import com.finartz.homework.TicketService.repositories.TicketRepository;
import com.finartz.homework.TicketService.service.FlightService;
import com.finartz.homework.TicketService.domain.embeddable.Seat;
import com.finartz.homework.TicketService.util.FlightClass;
import com.finartz.homework.TicketService.util.SearchType;
import com.finartz.homework.TicketService.util.SeatStatus;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@RequiredArgsConstructor
@Service
public class FlightServiceImpl implements FlightService {
    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;
    private final AirlineRepository airlineRepository;
    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;

    /**Ekleme
     *
     * @param flightDto         Eklenecek flight
     * @return                  Eklenen flight
     * @throws ArrivalBeforeDepartureException  Eklenmek istenen flight kalkis ve inis saati kontrolu
     * @throws ApiException     AirlineId, DepartureId,ArrivalId bulunamaz ise,
     *                          Ucus kalkis ve varis havaalani ayni sehirde ise
     */
    @Override
    public FlightResponseDTO saveFlight(FlightRequestDTO flightDto) throws ArrivalBeforeDepartureException, ApiException {
        Flight flight = handleSaveFlight(modelMapper.map(flightDto, Flight.class), flightDto);
        flight.setSeatsEmpty();     //Flight yeni eklendigi icin, tum ucusları default hale getir
        flightRepository.save(flight);

        return modelMapper.map(flight, FlightResponseDTO.class);
    }

    /**
     * Guncelleme
     *
     * @param id            Guncellenecek flight id'si
     * @param flightDto     Guncellenecek flight'in yeni alanları
     * @return              Guncellenen Flight'in modeli
     * @throws ArrivalBeforeDepartureException Eklenmek istenen flight kalkis ve inis saati kontrolu
     * @throws ApiException AirlineId, DepartureId,ArrivalId bulunamaz ise,
     *                      Flight kalkis ve varis havaalani ayni sehirde ise
     */
    @Override
    public FlightResponseDTO updateFlight(String id, FlightRequestDTO flightDto) throws ApiException, ArrivalBeforeDepartureException {
        Flight flight = null;
        try {
            flight = flightRepository.findById(id).get();
        } catch (NoSuchElementException ex) {   //flightId bulunamaz ise
            throw new ApiException("flightId Not Found", id.getClass(), "flightId", id);
        }

        flight.setDepartureDate(flightDto.getDepartureDate());
        flight.setArrivalDate(flightDto.getArrivalDate());
        flight.setPriceEconomic(flightDto.getPriceEconomic());
        flight.setPriceBusiness(flightDto.getPriceBusiness());
        flight.setCapasityEconomic(flightDto.getCapasityEconomic());
        flight.setCapasityBusiness(flightDto.getCapasityBusiness());
        flight = handleSaveFlight(flight, flightDto);

        flight.setSeatsBusiness(handleOldSeats(flight,FlightClass.BUSINESS));
        flight.setSeatsEconomic(handleOldSeats(flight,FlightClass.ECONOMI));

        return modelMapper.map(flightRepository.save(flight), FlightResponseDTO.class);
    }

    /**
     * Ekleme/ Guncelleme islemini hata kontrolu ile yapar.
     *
     * @param flight        Kaydedilecek Airport'un modeli
     * @param flightDto     Hata durumunda anlamli mesaj döndurmek icin kullanilacak.
     * @return              Kaydedilen Airport'un modeli
     * @throws ApiException airlineId, departureId, ArrivalId yok ise,
     *                      Flight kalkis ve varis havaalani ayni sehirde ise
     * @throws ArrivalBeforeDepartureException  Eklenmek istenen flight kalkis ve inis saati kontrolu
     */
    private Flight handleSaveFlight(Flight flight, FlightRequestDTO flightDto) throws ApiException, ArrivalBeforeDepartureException {
        try {
            String airlineId = flightDto.getAirlineId();
            Airline airline = airlineRepository.findById(airlineId).get();
            flight.setAirline(airline);
        } catch (NoSuchElementException ex) {   //airlineId bulunamaz ise
            throw new ApiException("airlineId Not Found", flightDto.getClass(), "airlineId", flightDto.getAirlineId());
        }

        try {
            String departureAirportId = flightDto.getDepartureAirportId();
            Airport departureAirport = airportRepository.findById(departureAirportId).get();
            flight.setDeparture(departureAirport);
        } catch (NoSuchElementException ex) {   //airportId bulunamaz ise
            throw new ApiException("departureId Not Found", flightDto.getClass(), "departureId", flightDto.getAirlineId());
        }

        try {
            String arrivalAirportId = flightDto.getArrivalAirportId();
            Airport arrivalAirport = airportRepository.findById(arrivalAirportId).get();
            flight.setArrival(arrivalAirport);
        } catch (NoSuchElementException ex) {   //arrivalId bulunamaz ise
            throw new ApiException("arrivalId Not Found", flightDto.getClass(), "arrivalId", flightDto.getAirlineId());
        }


        //Arrival ve Departure airportlar ayni sehirde ise
        if (flight.getArrival().getCity().equalsIgnoreCase(flight.getDeparture().getCity())) {
            throw new ApiException("Cannot fly between the same cities",
                    flightDto.getClass(), "departureAirportId,arrivalAirportId", flightDto.getAirlineId());
        }

        //ArrivalDate, DepartureDate'den once ise
        if (flight.getArrivalDate().isBefore(flight.getDepartureDate())) {
            throw new ArrivalBeforeDepartureException(flight.getArrivalDate(), flight.getDepartureDate());
        }

        //ArrivalDate ve DepartureDate'e gore duration belirle
        String duration = Duration.between(flight.getArrivalDate(), flight.getDepartureDate()).toString();
        flight.setDuration(duration.replace("-", " ").substring(3));

        return flight;
    }

    /**
     * Kapasite degişirse, yolculari cikar veya kapasite artarsa tabloyu güncelle
     *
     * @param updatedFlight Flight'in guncellenen hali
     * @param flightClass   İslem yapilacak FlighClass (BUSINESS or ECONOMY)
     * @return              Yeni koltuk düzeni listesi
     */
    private Map<String,Seat> handleOldSeats(Flight updatedFlight,FlightClass flightClass){
        Map<String,Seat> oldSeats;
        int oldCapasity;
        int updatedCapasity;

        if(flightClass == FlightClass.BUSINESS){    //Business ise
            oldSeats = updatedFlight.getSeatsBusiness();
            updatedCapasity = updatedFlight.getCapasityBusiness();
        }else{                                      //Economy ise
            oldSeats = updatedFlight.getSeatsEconomic();
            updatedCapasity = updatedFlight.getCapasityEconomic();
        }
        oldCapasity = oldSeats.size();

        if(oldCapasity < updatedCapasity){  //Kapasite artirildi ise
            for(int i = oldSeats.size() + 1 ; i <= updatedCapasity;i++){    //Yeni koltuklar
                oldSeats.put(""+i,new Seat( SeatStatus.empty,null));  //Default hale getir.
            }
        }
        else if(oldCapasity > updatedCapasity){ //Kapasite azaltildi ise
            Iterator<String> it2 = oldSeats.keySet().iterator();
            while (it2.hasNext()) {     //Her bir koltuk
                String key = it2.next();
                if (Integer.parseInt(key) > updatedCapasity) {  //Koltuk no, yeni kapasiteden büyük ise
                    it2.remove();   //Koltuk no'yu kaldir
                    //Ticketi kaldir.
                    Ticket ticket = ticketRepository.findByFlightAndNoAndFlightClass(updatedFlight, key, FlightClass.BUSINESS);
                    if (ticket != null)
                        ticketRepository.delete(ticket);
                }
            }
        }
        return oldSeats;
    }

    /**
     * Sil
     *
     * @param id            Silinecek flight id'si
     * @throws ApiException Flight id bulunamazsa
     */
    @Override
    public void deleteFlight(String id) throws ApiException {
        Flight flight;
        try {
            flight = flightRepository.findById(id).get();
        } catch (NoSuchElementException ex) {   //FlightId yok ise
            throw new ApiException("FlightId Not Found", id.getClass(), "flightId", id);
        }
        flightRepository.delete(flight);
    }

    /**
     * Hepsini cek
     *
     * @return Veritabanındaki tüm flightlar.
     */
    @Override
    public List<FlightResponseDTO> getAll() {
        return flightListToFlightResponseDtoList(flightRepository.findAll());
    }


    /**
     * Id ile Arama
     *
     * @param id     Alinmak istenen Airline id'si
     * @return       Istenen flight modeli - Id bulunmaz ise null döner.
     */
    @Override
    public FlightResponseDTO getFlight(String id) {
        try {
            return modelMapper.map(flightRepository.findById(id).get(), FlightResponseDTO.class);
        } catch (NoSuchElementException ex) {   //FlightId yok ise
            return null;
        }
    }

    /**
     * Arline Name ile Arama - Havayoluna ait aktif ucuslar.
     *
     * @param airlineName   Ucuslari cekilmek istenen Airline ismi.
     * @return              Istenen Flight modellari.
     */
    @Override
    public List<FlightResponseDTO> getFlightsByAirlineName(String airlineName) {
        List<FlightResponseDTO> flightResponseDTOList = new ArrayList<>();
        List<Airline> airlines = airlineRepository.findByNameIsContainingIgnoreCase(airlineName);
        airlines.stream().forEach(airline -> {  //Aramaya uygun her bir havayolu
            airline.getActiveFlights().stream().forEach(flight -> {     //Ve bu havayolunun her bir ucusu
                flightResponseDTOList.add(modelMapper.map(flight, FlightResponseDTO.class));  //Listeye ekle(Dönüstürerek)
            });
        });
        return flightResponseDTOList;
    }

    /**
     * Kalkış Havaalanina(Departure) göre Arama - Havaalanindan KALKAN Flightlar.
     *
     * @param searchType    Kalkis Havaalani neye görene aranacak?
     *                      Isime göre(byname) - sadece isime göre arar,
     *                      sehire göre(bycity) - sadece sehire göre arar,
     *                      isim veya sehire göre(bynameorcity) - hem isim hem sehir icinde arar,

     * @param nameOrCity    Aranacak kelime
     * @return              Havaalanı/Havaalanlarından kalkan ucus modelleri
     */
    @Override
    public List<FlightResponseDTO> getFlightsByDeparture(SearchType searchType, String nameOrCity) {
        if (searchType == SearchType.byname) {        /*Kalkış Havaalanı İsmi ile Arama*/
            return getDepartureFlightResponseDtoListFromAirportList(
                    airportRepository.findByNameIsContainingIgnoreCase(nameOrCity));
        } else if (searchType == SearchType.bycity) {  /*Kalkış Havaalanı Şehiri ile Arama*/
            return getDepartureFlightResponseDtoListFromAirportList(
                    airportRepository.findByCityIsContainingIgnoreCase(nameOrCity));
        } else {                                      /*Kalkış Havaalanı Şehiri VEYA ismi ile Arama*/
            return getDepartureFlightResponseDtoListFromAirportList(
                    airportRepository.findByNameOrCity(nameOrCity, nameOrCity));
        }
    }

    /**Varis Havaalanına(Arrival) göre Arama - Havaalanına INEN Flightlar.
     *
     * @param searchType    Varis Havaalanı neye görene aranacak?
     *                      Isime göre(byname) - sadece isime göre arar,
     *                      sehire göre(bycity) - sadece sehire göre arar,
     *                      isim veya sehire göre(bynameorcity) - hem isim hem sehir icinde arar,
     * @param nameOrCity    Aranacak kelime
     * @return              Havaalanı/Havaalanlarından kalkan ucus modelleri
     */
    @Override
    public List<FlightResponseDTO> getFlightsByArrival(SearchType searchType, String nameOrCity) {
        if (searchType == SearchType.byname) {        /*Kalkış Havaalanı İsmi ile Arama*/
            return getArrivalFlightResponseDtoListFromAirportList(
                    airportRepository.findByNameIsContainingIgnoreCase(nameOrCity));
        } else if (searchType == SearchType.bycity) {  /*Kalkış Havaalanı Şehiri ile Arama*/
            return getArrivalFlightResponseDtoListFromAirportList(
                    airportRepository.findByCityIsContainingIgnoreCase(nameOrCity));
        } else {                                      /*Kalkış Havaalanı Şehiri VEYA ismi ile Arama*/
            return getArrivalFlightResponseDtoListFromAirportList(
                    airportRepository.findByNameOrCity(nameOrCity, nameOrCity));
        }
    }

    /**
     *Kalkış Havaalanına(Departure)  VE  Varis Havaalanına(Arrival) göre Arama - Departure'dan kalkip, Arrivalda inen flightlar.
     *
     * @param searchType   Varis ve kalkis Havaalanı neye görene aranacak?
     *                     Isime göre(byname) - sadece isime göre arar,
     *                     sehire göre(bycity) - sadece sehire göre arar,
     *                     isim veya sehire göre(bynameorcity) - hem isim hem sehir icinde arar,
     * @param departure    Kalkis havaalanı
     * @param arrival      Varis havaalani
     * @return             Havaalani/Havaalanlarindan kalkan ucus modelleri
     */
    @Override
    public FlightsResponseDTO getFlightsByDepartureAndArrival(SearchType searchType, String departure, String arrival) {
        FlightsResponseDTO result = new FlightsResponseDTO();
        List<Flight> directFlightList;
        List<FlightResponseDTO> directFlightDtoList;

        if (searchType == SearchType.byname) {        //Kalkış Havaalanı İsmi ile Arama
            directFlightList = flightRepository.findByDepartureAndArrivalName(departure, arrival);
        } else if (searchType == SearchType.bycity) {  //Kalkış Havaalanı Şehiri ile Arama
            directFlightList = flightRepository.findByDepartureAndArrivalCity(departure, arrival);
        } else {                                      //Kalkış Havaalanı Şehiri VEYA ismi ile Arama
            directFlightList = flightRepository.findByDepartureAndArrivalCityOrName(departure, arrival);
        }

        //Set direct flights
        directFlightDtoList = flightListToFlightResponseDtoList(directFlightList);
        result.setDirectFlights(directFlightDtoList);
        //Set indirect flights
        result.setIndirectFlights(getIndirectFlights(searchType, departure, arrival));

        return result;
    }

    /**
     * Departure ve Arrival search valuelerine gore, indirect ucuslari bulur.
     * @param searchType    Ucus hangi FlightClass'ta isteniyor (BUSINESS or ECONOMY)
     * @param departure     Kalkis Havalaani arama degeri
     * @param arrival       Kalkis Havalaani arama degeri
     * @return              Indirect ucuslarin listesi
     */
    private List<IndirectFlightDTO> getIndirectFlights(SearchType searchType, String departure, String arrival) {
        ArrayList<IndirectFlightDTO> indirectFlights = new ArrayList<>();
        List<FlightResponseDTO> possibleFirstFlights = this.getFlightsByDeparture(searchType, departure); //Olası ilk Ucuslar
        possibleFirstFlights.stream().forEach(possibleFirstFlight -> {
            AirportResponseDTO possibleArrival = possibleFirstFlight.getArrival(); //Olası ilk varis noktası
            possibleArrival.getDepartureFlights().stream().forEach(possibleSecondFlight -> { //Olası ilk varış noktasından kalkan ucusları al
                //Olası 2. ucusun varis noktası, istenen nokta'yı iceriyor ise;
                if (possibleSecondFlight.getArrival().getName().toUpperCase().contains(arrival.toUpperCase())) {
                    if (possibleSecondFlight.getDepartureDate().isAfter(possibleFirstFlight.getArrivalDate())) { //2.nin kalkışı 1.den sonra ise
                        Duration duration = Duration.between(possibleFirstFlight.getArrivalDate(), possibleSecondFlight.getDepartureDate());
                        if (duration.toHours() <= 12) { //Yolcu 12 saatten fazla beklemeyecekse
                            indirectFlights.add(new IndirectFlightDTO(possibleFirstFlight, possibleSecondFlight)); //Ekle
                        }
                    }
                }
            });
        });
        return indirectFlights;
    }

    /**
     * Airport List icindeki Airportlarin her birine inen ucusları, FlightResponseDto'ya donustutur.
     *
     * @param airports Bu list icindeki Airport nesnelerine inen ucuslar, AirlineResponseDto listesine dönüsür.
     * @return          Dönüstürülen FlightResponseDto'larin bulundugu list.
     */
    private List<FlightResponseDTO> getArrivalFlightResponseDtoListFromAirportList(List<Airport> airports) {
        List<FlightResponseDTO> flightResponseDTOList = new ArrayList<>();
        airports.stream().forEach(airport -> { //Aramaya uygun her bir havaalanı
            airport.getArrivalFlights().stream().forEach(flight -> {  //Ve bu havaalanına inen her bir ucus
                flightResponseDTOList.add(modelMapper.map(flight, FlightResponseDTO.class));  //Listeye ekle(Dönüstürerek)
            });
        });
        return flightResponseDTOList;
    }

    /**
     * Airport List icindeki Airportlarin her birinden kalkan ucusları, FlightResponseDto'ya donustutur.
     *
     * @param airports Bu list icindeki Airport nesnelerinden kalkan ucuslar, AirlineResponseDto listesine dönüsür.
     * @return          Dönüstürülen FlightResponseDto'larin bulundugu list.
     */
    private List<FlightResponseDTO> getDepartureFlightResponseDtoListFromAirportList(List<Airport> airports) {
        List<FlightResponseDTO> flightResponseDTOList = new ArrayList<>();
        airports.stream().forEach(airport -> { //Aramaya uygun her bir havaalanı
            airport.getDepartureFlights().stream().forEach(flight -> {  //Ve bu havaalanına inen her bir uçak
                flightResponseDTOList.add(modelMapper.map(flight, FlightResponseDTO.class));  //Listeye ekle(Dönüstürerek)
            });
        });
        return flightResponseDTOList;
    }

    /**
     * Kendisine gelen Flight List'i, FlightResponseDtoList'e dönüstürür.
     *
     * @param flights   Donusturulecek Flight'larin bulundugu list.
     * @return          Donusturulen FlightResponseDtoList.
     */
    private List<FlightResponseDTO> flightListToFlightResponseDtoList(List<Flight> flights) {
        List<FlightResponseDTO> flightResponseDTOList = new ArrayList<>();
        flights.stream().forEach(flight -> {  //Her bir flight
            flightResponseDTOList.add(modelMapper.map(flight, FlightResponseDTO.class));  //Listeye ekle(Dönüstürerek)
        });
        return flightResponseDTOList;
    }
}