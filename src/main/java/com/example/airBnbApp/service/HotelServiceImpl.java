package com.example.airBnbApp.service;

import com.example.airBnbApp.dto.HotelDto;
import com.example.airBnbApp.entity.Hotel;
import com.example.airBnbApp.exception.ResourceNotFoundException;
import com.example.airBnbApp.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService{

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;

    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("Creating a new Hotel with name: {}",hotelDto.getName());
        Hotel hotel=modelMapper.map(hotelDto,Hotel.class);//convert hotelDto to hotel as Hitel.class
        hotel.setActive(false);//hotel is intall not active so we are not to search in inventoty
        hotel=hotelRepository.save(hotel);
        log.info("Creating a new Hotel with ID: {}",hotelDto.getId());
        return modelMapper.map(hotel,HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long id) {
        log.info("Getting hotel with ID: {}",id);
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found with id:"+id));
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto updateHotelById(Long id, HotelDto hotelDto) {
        log.info("Updating hotel with ID: {}",id);
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found with id:"+id));
        modelMapper.map(hotelDto,hotel);
        hotel.setId(id);
        hotel=hotelRepository.save(hotel);
        return modelMapper.map(hotel,HotelDto.class);
    }

    @Override
    public void deleteHotelById(Long id) {
        boolean exist=hotelRepository.existsById(id);
        if(!exist){
            throw new ResourceNotFoundException("Hotel not found with id:"+id);
        }
        hotelRepository.deleteById(id);

        //TODO: delete future inventories for this hotel

    }

    @Override
    public void activateHotel(Long hotelId) {
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found with id:"+hotelId));
        hotel.setActive(true);
        //TODO: Create inventory for all the rooms for this hotel

    }
}
