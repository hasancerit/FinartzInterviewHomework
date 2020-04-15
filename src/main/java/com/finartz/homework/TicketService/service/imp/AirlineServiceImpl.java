package com.finartz.homework.TicketService.service.imp;

import com.finartz.homework.TicketService.domain.Airline;
import com.finartz.homework.TicketService.dto.request.AirlineRequestDTO;
import com.finartz.homework.TicketService.dto.response.AirlineResponseDTO;
import com.finartz.homework.TicketService.exception.exception.CustomAlreadyTaken;
import com.finartz.homework.TicketService.exception.exception.CustomNotFound;
import com.finartz.homework.TicketService.repositories.AirlineRepository;
import com.finartz.homework.TicketService.service.AirlineService;
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
public class AirlineServiceImpl implements AirlineService {
    private final AirlineRepository airlineRepository;
    private final ModelMapper modelMapper;


    /**
     * Ekleme
     *
     * @param airlineReqDTO     Kaydedilecek Airline'in modeli
     * @return                  Kaydedilen Airline'in modeli
     * @throws CustomAlreadyTaken     Airline Name zaten varsa
     * @see #handleSaveAirline(Airline, AirlineRequestDTO)
     */
    @Override
    public AirlineResponseDTO saveAirline(AirlineRequestDTO airlineReqDto) throws CustomAlreadyTaken {
        Airline airline = modelMapper.map(airlineReqDto, Airline.class);
        airline = handleSaveAirline(airline, airlineReqDto);

        return modelMapper.map(airline, AirlineResponseDTO.class);
    }


    /**
     * Guncelleme
     *
     * @param id                Guncellenecek Airline id'si
     * @param airlineReqDTO     Guncellenecek Airline'in yeni alanları
     * @return                  Guncellenen Airline'in modeli
     * @throws CustomNotFound         Airline id bulunamazsa
     * @throws CustomAlreadyTaken     Airline name zaten alinmissa
     * @see #handleSaveAirline(Airline, AirlineRequestDTO)
     */
    @Override
    public AirlineResponseDTO updateAirline(String id, AirlineRequestDTO airlineReqDto) throws CustomAlreadyTaken, CustomNotFound {
        Airline airline = null;
        try {
            airline = airlineRepository.findById(id).get();
        } catch (NoSuchElementException ex) {   //Airline id bulunamazsa
            throw new CustomNotFound(id.getClass(), "airlineId", id);
        }

        airline.setName(airlineReqDto.getName());
        airline.setDesc(airlineReqDto.getDesc());

        return modelMapper.map(handleSaveAirline(airline, airlineReqDto), AirlineResponseDTO.class);
    }


    /**
     * Ekleme/ Guncelleme islemini hata kontrolu ile yapar.
     *
     * @param airline       Kaydedilecek Airline'in modeli
     * @param airlineReqDTO Hata durumunda anlamli mesaj döndurmek icin kullanilacak.
     * @return              Kaydedilen Airline'in modeli
     * @throws CustomAlreadyTaken Airline Name zaten varsa
     */
    private Airline handleSaveAirline(Airline airline, AirlineRequestDTO airlineReqDto) throws CustomAlreadyTaken {
        try {
            return airlineRepository.save(airline);
        } catch (DataIntegrityViolationException e) {   //Airline Name zaten varsa
            throw new CustomAlreadyTaken("name is already taken.", airlineReqDto.getClass(), "name", airlineReqDto.getName());
        }
    }


    /**
     * Silme
     *
     * @param id              Silinecek Airline id'si
     * @throws CustomNotFound Airline id bulunamazsa
     */
    @Override
    public void deleteAirline(String id) throws CustomNotFound{
        try {
            airlineRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {   //Airline id bulunamazsa
            throw new CustomNotFound(id.getClass(), "airlineId", id);
        }
    }


    /**
     * Hepsini cek
     *
     * @return Veritabanındaki tüm Airline'lar.
     * @see #airlineListToAirlineDtoList(List)
     */
    @Override
    public List<AirlineResponseDTO> getAll() {
        return airlineListToAirlineDtoList(airlineRepository.findAll());
    }

    /**
     * Id İle Arama
     *
     * @param id                Alinmak istenen Airline id'si
     * @return                  Istenen airline modeli
     * @throws CustomNotFound   Airline id bulunamazsa
     */


    @Override
    public AirlineResponseDTO getAirline(String id) throws CustomNotFound {
        try {
            return modelMapper.map(airlineRepository.findById(id).get(), AirlineResponseDTO.class);
        } catch (NoSuchElementException ex) {   //Id bulunmaz ise
            throw new CustomNotFound(id.getClass(), "airlineId", id);
        }
    }


    /**
     * Isim ile arama
     *
     * @param name              Alinmak istenen Airline isimi.
     * @return                  Istenen airline modeli - Bu isimde airline yoksa bos liste döner.
     * @throws CustomNotFound   Aranan name bulunamazsa
     * @see #airlineListToAirlineDtoList(List)
     */
    @Override
    public List<AirlineResponseDTO> getAirlinesByName(String name) throws CustomNotFound {
        List<Airline> airlines = airlineRepository.findByNameIsContainingIgnoreCase(name);

        if(airlines==null || airlines.size() == 0  )
            throw new CustomNotFound(name.getClass(), "value", name);
        return airlineListToAirlineDtoList(airlines);
    }


    /**
     * AirlineList'in icinde bulunan her Airline nesnesini, AirlineResponseDto nesnesine dönüstür
     *
     * @param airlines  Bu list icindeki airline nesneleri, AirlineResponseDto listesine dönüsür.
     * @return          Dönüstürülen AirlineResponseDto'larin bulundugu list.
     */
    private List<AirlineResponseDTO> airlineListToAirlineDtoList(List<Airline> airlines) {
        List<AirlineResponseDTO> airlineResponseDTOList = new ArrayList<>();
        airlines.stream().forEach(airline -> {                                              //Airline icindeki her bir airline
            airlineResponseDTOList.add(modelMapper.map(airline, AirlineResponseDTO.class)); //AirlineResponseDto'ya dönüstür ve ekle
        });
        return airlineResponseDTOList;
    }
}
