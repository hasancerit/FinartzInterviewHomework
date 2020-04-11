package com.finartz.homework.TicketService.service.imp;

import com.finartz.homework.TicketService.domain.Airline;
import com.finartz.homework.TicketService.domain.Airport;
import com.finartz.homework.TicketService.dto.request.AirportRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirportResponseDTO;
import com.finartz.homework.TicketService.exception.exception.ApiException;
import com.finartz.homework.TicketService.repositories.AirportRepository;
import com.finartz.homework.TicketService.service.AirportService;
import com.finartz.homework.TicketService.util.SearchType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class AirportServiceImpl implements AirportService {
    private final AirportRepository airportRepository;
    private final ModelMapper modelMapper;

    /**
     * Ekleme
     *
     * @param airportDto    Kaydedilecek Airport'un modeli
     * @return              Kaydedilen Airport'un modeli
     * @throws ApiException Airport Name zaten varsa
     */
    @Override
    public AirportResponseDTO saveAirport(AirportRequestDTO airportDto) throws ApiException {
        Airport airport = handleSaveAirport(modelMapper.map(airportDto,Airport.class),airportDto);
        return modelMapper.map(airport,AirportResponseDTO.class);
    }

    /**
     * Guncelleme
     *
     * @param id            Guncellenecek Airport id'si
     * @param airportDto    Guncellenecek Airport'un yeni alanları
     * @return              Guncellenen Airport'un modeli
     * @throws ApiException Airport id bulunamazsa
     */
    @Override
    public AirportResponseDTO updateAirport(String id, AirportRequestDTO airportDto) throws ApiException {
        Airport airport = null;
        try {
            airport = airportRepository.findById(id).get();
        } catch(NoSuchElementException ex) {    //Airport id bulunamazsa
            throw new ApiException("airportId Not Found",id.getClass(),"airportId",id);
        }

        airport.setCity(airportDto.getCity());
        airport.setName(airportDto.getName());
        airport.setDesc(airportDto.getDesc());
        return modelMapper.map(handleSaveAirport(airport,airportDto),AirportResponseDTO.class);
    }

    /**
     * Ekleme/ Guncelleme islemini hata kontrolu ile yapar.
     *
     * @param airport       Kaydedilecek Airport'un modeli
     * @param airportDto    Hata durumunda anlamli mesaj döndurmek icin kullanilacak.
     * @return              Kaydedilen Airport'un modeli
     * @throws ApiException Airport Name zaten varsa
     */
    private Airport handleSaveAirport(Airport airport,AirportRequestDTO airportDto) throws ApiException {
        try {
            airport = airportRepository.save(airport);
        } catch (DataIntegrityViolationException e) {   //Airport Name zaten varsa
            throw new ApiException("name is already taken.",airportDto.getClass(),
                    "name",airportDto.getName());
        }
        return airport;
    }

    /**
     * Silme
     *
     * @param id            Silinecek Airport id'si
     * @throws ApiException Airport id bulunamazsa
     */
    @Override
    public void deleteAirport(String id) throws ApiException {
        Airport airport;
        try{    //Airport id bulunamazsa
            airport = airportRepository.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new ApiException("airportId Not Found",id.getClass(),"airportId",id);
        }
        airportRepository.delete(airport);
    }

    /**
     * Hepsini cek
     *
     * @return Veritabanındaki tüm Airport'lar.
     */
    @Override
    public List<AirportResponseDTO> getAll() {
        return airportListToAirpostDtoList(airportRepository.findAll());
    }

    /**
     * Id İle Arama
     *
     * @param id
     * @return
     */
    @Override
    public AirportResponseDTO getAirport(String id) {
        try{
            return modelMapper.map(airportRepository.findById(id).get(),AirportResponseDTO.class);
        }catch (NoSuchElementException ex){
            return null;
        }
    }

    /**
     * Name-City ile arama - Airportun isimi veya Airportun bulundugu sehire göre arama
     *
     * @param searchType    Isime göre(byname) - sadece isime göre arar,
     *                      sehire göre(bycity) - sadece sehire göre arar,
     *                      isim VEYA sehire göre(bynameorcity) - hem isim hem sehir icinde arar,
     * @param nameOrCity    Aranacak kelime
     * @return              Bulunan Airport modelleri.
     */
    @Override
    public List<AirportResponseDTO> getAirports(SearchType searchType, String nameOrCity) {
        List<Airport> airports;
        if(searchType == SearchType.byname){        //Airport ismi ile arama
            airports = airportRepository.findByNameIsContainingIgnoreCase(nameOrCity);
        }else if(searchType == SearchType.bycity){  //Airport sehiri ile arama
            airports = airportRepository.findByCityIsContainingIgnoreCase(nameOrCity);
        }else{                                      //Airport sehiri VEYA ismi ile Arama
            airports = airportRepository.findByNameOrCity(nameOrCity,nameOrCity);
        }
        return airportListToAirpostDtoList(airports);
    }

    /**
     * AirportList icindeki tüm Airport nesnelerini, AirpostResponseDto nesnesine cevir.
     *
     * @param airports  Bu list icindeki Airport nesneleri, AirpostResponseDto listesine dönüsür.
     * @return          Dönüstürülen AirpostResponseDto'larin bulundugu list.
     */
    private List<AirportResponseDTO> airportListToAirpostDtoList(List<Airport> airports){
        List<AirportResponseDTO> airportResponseDTOList =new ArrayList<>();
        airports.stream().forEach(airport -> {  //Her bir airport nesnesi
            airportResponseDTOList.add(modelMapper.map(airport,AirportResponseDTO.class)); //AirpostResponseDto'ya dönüstür ve ekle
        });
        if(airportResponseDTOList.size() == 0) return null; //Size 0 ise null döndür.
        return airportResponseDTOList;
    }
}
