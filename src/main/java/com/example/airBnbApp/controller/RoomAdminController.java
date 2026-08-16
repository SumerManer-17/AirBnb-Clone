package com.example.airBnbApp.controller;

import com.example.airBnbApp.dto.RoomDto;
import com.example.airBnbApp.entity.Room;
import com.example.airBnbApp.repository.RoomRepository;
import com.example.airBnbApp.service.HotelService;
import com.example.airBnbApp.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hotels/{hotelId}/rooms")
@RequiredArgsConstructor
@Slf4j
public class RoomAdminController {
    private final RoomRepository roomRepository;

    private final RoomService roomService;
    private final HotelService hotelService;

    @PostMapping
    public ResponseEntity<RoomDto>createNewRoom(@PathVariable Long hotelId,
                                                @RequestBody RoomDto roomDto){
        RoomDto room=roomService.createNewRoom(hotelId,roomDto);
        return new ResponseEntity<>(room, HttpStatus.CREATED);
    }

    @GetMapping //Get all rooms in hotel
    public ResponseEntity<List<RoomDto>> getAllRoomsInHotel(@PathVariable Long hotelId){
        return ResponseEntity.ok(roomService.getAllRoomsInHotel(hotelId));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDto>getRoomById(@PathVariable Long hotelId,@PathVariable Long roomId){
        return ResponseEntity.ok(roomService.getRoomById(roomId));
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void>deleteRoomById(@PathVariable Long roomId){
        roomService.deleteRoomById(roomId);
        return ResponseEntity.noContent().build();
    }

}
