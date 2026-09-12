package com.example.airBnbApp.service;

import com.example.airBnbApp.dto.RoomDto;
import com.example.airBnbApp.entity.Hotel;
import com.example.airBnbApp.entity.Room;
import com.example.airBnbApp.entity.User;
import com.example.airBnbApp.exception.ResourceNotFoundException;
import com.example.airBnbApp.exception.UnAuthorizedException;
import com.example.airBnbApp.repository.HotelRepository;
import com.example.airBnbApp.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final InventoryService inventoryService;

    @Override
    public RoomDto createNewRoom(Long hotelId, RoomDto roomDto) {
        Hotel hotel=hotelRepository
                .findById(hotelId)
                .orElseThrow(()->
                        new ResourceNotFoundException
                                ("Hotel not found with id:"+hotelId));
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorizedException("This user does not own this hotel with id" +hotelId);
        }

        Room room=modelMapper.map(roomDto,Room.class);
        room.setHotel(hotel);
        room=roomRepository.save(room);

        //TODO: Create inventory ASA room is created

//        if(hotel.getActive()){
//            inventoryService.intializeRoomForYear(room);
//        }

        // Replace lines 48-52 in RoomServiceImpl.java:
        inventoryService.intializeRoomForYear(room);


        return modelMapper.map(room,RoomDto.class);
    }

    @Override
    public List<RoomDto> getAllRoomsInHotel(Long hotelId) {
        Hotel hotel=hotelRepository.findById(hotelId)
                .orElseThrow(()->new ResourceNotFoundException
                        ("Hotel not found with id:"+hotelId));

        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorizedException("This user does not own this hotel with id" +hotelId);
        }

        return hotel.getRooms()
                .stream().map((element) ->
                        modelMapper.map(element, RoomDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public RoomDto getRoomById(Long roomId) {
        Room room=roomRepository.findById(roomId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Room not found with id:"+roomId));
        return modelMapper.map(room,RoomDto.class);
    }
    @Transactional
    @Override
    public void deleteRoomById(Long roomId) {
        Room room=roomRepository.findById(roomId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Room not found with id:"+roomId));

        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(room.getHotel().getOwner())){
            throw new UnAuthorizedException("This user does not own this room with id" +roomId);
        }

        inventoryService.deleteFutureInverntories(room);
        roomRepository.deleteById(roomId);
    }



}
