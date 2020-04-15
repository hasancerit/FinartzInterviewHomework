package com.finartz.homework.TicketService.service.imp;

import com.finartz.homework.TicketService.domain.Airport;
import com.finartz.homework.TicketService.dto.request.AirportRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirportResponseDTO;
import com.finartz.homework.TicketService.exception.exception.CustomAlreadyTaken;
import com.finartz.homework.TicketService.exception.exception.CustomNotFound;
import com.finartz.homework.TicketService.repositories.AirportRepository;
import com.finartz.homework.TicketService.service.AirportService;
import com.finartz.homework.TicketService.util.SearchType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
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
     * @param airportReqDto Kaydedilecek Airport'un modeli
     * @return              Kaydedilen Airport'un modeli
     * @throws CustomAlreadyTaken Airport Name zaten varsa
     * @see #handleSaveAirport(Airport, AirportRequestDTO)
     */
    @Override
    public AirportResponseDTO saveAirport(AirportRequestDTO airportReqDto) throws CustomAlreadyTaken {
        Airport airport = modelMapper.map(airportReqDto,Airport.class);
        airport = handleSaveAirport(airport,airportReqDto);

        return modelMapper.map(airport,AirportResponseDTO.class);
    }


    /**
     * Guncelleme
     *
     * @param id                Guncellenecek Airport id'si
     * @param airportReqDto     Guncellenecek Airport'un yeni alanları
     * @return                  Guncellenen Airport'un modeli
     * @throws CustomNotFound         Airport id bulunamazsa
     * @throws CustomAlreadyTaken     Airport name zaten alinmissa
     * @see #handleSaveAirport(Airport, AirportRequestDTO)
     */
    @Override
    public AirportResponseDTO updateAirport(String id, AirportRequestDTO airportReqDto) throws CustomAlreadyTaken, CustomNotFound {
        Airport airport = null;
        try {
            airport = airportRepository.findById(id).get();
        } catch(NoSuchElementException ex) {    //Airport id bulunamazsa
            throw new CustomNotFound(id.getClass(),"airportId",id);
        }

        airport.setCity(airportReqDto.getCity());
        airport.setName(airportReqDto.getName());
        airport.setDesc(airportReqDto.getDesc());
        return modelMapper.map(handleSaveAirport(airport,airportReqDto),AirportResponseDTO.class);
    }


    /**
     * Ekleme/ Guncelleme islemini hata kontrolu ile yapar.
     *
     * @param airport       Kaydedilecek Airport'un modeli
     * @param airportReqDto Hata durumunda anlamli mesaj döndurmek icin kullanilacak.
     * @return              Kaydedilen Airport'un modeli
     * @throws CustomAlreadyTaken Airport Name zaten varsa
     */
    private Airport handleSaveAirport(Airport airport,AirportRequestDTO airportReqDto) throws CustomAlreadyTaken {
        try {
            return  airportRepository.save(airport);
        } catch (DataIntegrityViolationException e) {   //Airport Name zaten varsa
            throw new CustomAlreadyTaken("name is already taken.",airportReqDto.getClass(), "name",airportReqDto.getName());
        }
    }


    /**
     * Silme
     *
     * @param id                Silinecek Airport id'si
     * @throws CustomNotFound   Airport id bulunamazsa
     */
    @Override
    public void deleteAirport(String id) throws CustomNotFound {
        try{
            airportRepository.deleteById(id);
        }catch (EmptyResultDataAccessException ex){ //Airport id bulunamazsa
            throw new CustomNotFound(id.getClass(),"airportId",id);
        }
    }


    /**
     * Hepsini cek
     *
     * @return Veritabanındaki tüm Airport'lar.
     * @see #airportListToAirpostDtoList(List)
     */
    @Override
    public List<AirportResponseDTO> getAll() {
        return airportListToAirpostDtoList(airportRepository.findAll());
    }


    /**
     * Id İle Arama
     *
     * @param id                Alinmak istenen Airport id'si
     * @return                  Istenen Airport modeli
     * @throws CustomNotFound   Airport id bulunamazsa
     */
    @Override
    public AirportResponseDTO getAirport(String id) throws CustomNotFound {
        try{
            return modelMapper.map(airportRepository.findById(id).get(),AirportResponseDTO.class);
        }catch (NoSuchElementException ex){
            throw new CustomNotFound(id.getClass(),"airportId",id);
        }
    }


    /**
     * Name-City ile arama - Airportun isimi veya Airportun bulundugu sehire göre arama
     *
     * @param searchType        Isime göre(byname) - sadece isime göre arar,
     *                          sehire göre(bycity) - sadece sehire göre arar,
     *                          isim VEYA sehire göre(bynameorcity) - hem isim hem sehir icinde arar,
     * @param nameOrCity        Aranacak kelime
     * @return                  Bulunan Airport modelleri.
     * @throws CustomNotFound   Aranan degerde airport bulunamazsa
     * @see #airportListToAirpostDtoList(List)
     */
    @Override
    public List<AirportResponseDTO> getAirports(SearchType searchType, String nameOrCity) throws CustomNotFound {
        List<Airport> airports;
        if(searchType == SearchType.byname){        //Airport ismi ile arama
            airports = airportRepository.findByNameIsContainingIgnoreCase(nameOrCity);
        }else if(searchType == SearchType.bycity){  //Airport sehiri ile arama
            airports = airportRepository.findByCityIsContainingIgnoreCase(nameOrCity);
        }else{                                      //Airport sehiri VEYA ismi ile Arama
            airports = airportRepository.findByNameOrCity(nameOrCity);
        }

        if(airports == null || airports.size() == 0) throw new CustomNotFound(nameOrCity.getClass(),"value",nameOrCity);
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
        return airportResponseDTOList;
    }

}
