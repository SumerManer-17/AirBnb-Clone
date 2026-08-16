package com.example.airBnbApp.service;

import com.example.airBnbApp.dto.RoomDto;
import com.example.airBnbApp.entity.Hotel;
import com.example.airBnbApp.entity.Room;
import com.example.airBnbApp.exception.ResourceNotFoundException;
import com.example.airBnbApp.repository.HotelRepository;
import com.example.airBnbApp.repository.RoomRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService{
    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;
    private final HotelRepository hotelRepository;

    @Override
    public RoomDto createNewRoom(Long hotelId, RoomDto roomDto) {
        Hotel hotel=hotelRepository
                .findById(hotelId)
                .orElseThrow(()->
                        new ResourceNotFoundException
                                ("Hotel not found with id:"+hotelId));
        Room room=modelMapper.map(roomDto,Room.class);
        room.setHotel(hotel);
        room=roomRepository.save(room);

        //TODO: Create inventory ASA room is created
        return modelMapper.map(room,RoomDto.class);
    }

    @Override
    public List<RoomDto> getAllRoomsInHotel(Long hotelId) {
        Hotel hotel=hotelRepository.findById(hotelId)
                .orElseThrow(()->new ResourceNotFoundException
                        ("Hotel not found with id:"+hotelId));
        return hotel.getRooms()
                .stream().map((element) ->
                        modelMapper.map(element, RoomDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public RoomDto getRoomById(Long roomId) {
        Room room=roomRepository.findById(roomId).orElseThrow(()->new ResourceNotFoundException("Room not found with id:"+roomId));
        return modelMapper.map(room,RoomDto.class);
    }

    @Override
    public void deleteRoomById(Long roomId) {
        boolean exists=roomRepository.existsById(roomId);
        if(!exists){
            throw new ResourceNotFoundException("Room not found with id"+roomId);
        }
        roomRepository.deleteById(roomId);
        //TODO: Delete all future inventories for this room
    }

}
