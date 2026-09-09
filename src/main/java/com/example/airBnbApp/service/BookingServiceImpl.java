package com.example.airBnbApp.service;

import com.example.airBnbApp.dto.BookingDTO;
import com.example.airBnbApp.dto.BookingRequest;
import com.example.airBnbApp.dto.GuestDto;
import com.example.airBnbApp.entity.*;
import com.example.airBnbApp.entity.enums.BookingStatus;
import com.example.airBnbApp.exception.ResourceNotFoundException;
import com.example.airBnbApp.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{
    private final GuestRepository guestRepository;
    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final ModelMapper modeMapper;

    @Override
    @Transactional
    public BookingDTO initialiseBooking(BookingRequest bookingRequest) {

        log.info("Intializing booking for hotel :{},room:{},date{}-{}",bookingRequest.getHotelId(),
                bookingRequest.getRoomId(),bookingRequest.getCheckInDate(),
                bookingRequest.getCheckOutDate());

        Hotel hotel=hotelRepository.findById(bookingRequest.getHotelId()).orElseThrow(()->
                new ResourceNotFoundException("Hotel not found with id: "+bookingRequest.getHotelId()));

        Room room=roomRepository.findById(bookingRequest.getRoomId()).orElseThrow(()->
                new ResourceNotFoundException("Room not found with id: "+bookingRequest.getRoomId()));

        List<Inventory>inventoryList=inventoryRepository
                .findAndLockAvailableInventory
                        (room.getId(), bookingRequest.getCheckInDate(),
                                bookingRequest.getCheckOutDate(),
                                bookingRequest.getRoomCount());


        long daysCount= ChronoUnit.DAYS.between(bookingRequest.getCheckInDate(),
                bookingRequest.getCheckOutDate())+1;

        if(inventoryList.size()!=daysCount){
            throw new IllegalStateException("Room is not available anymore");
        }

        //Reserve the room/ Update the booked count of Inventories

        for (Inventory inventory:inventoryList){
            inventory.setReservedCount(inventory.getReservedCount()+ bookingRequest.getRoomCount());
        }

        inventoryRepository.saveAll(inventoryList);

        //create the booking
         //Getting dummy user
        Booking booking= Booking.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .checkInDate(bookingRequest.getCheckOutDate())
                .checkOutDate(bookingRequest.getCheckOutDate())
                .user(getCurrentUser())
                .roomsCount(bookingRequest.getRoomCount())
                .amount(BigDecimal.TEN)
                .build();

        booking=bookingRepository.save(booking);
        return modeMapper.map(booking,BookingDTO.class);

    }

    @Override
    @Transactional
    public BookingDTO addGuests(Long bookingId, List<GuestDto> guestDtoList) {

        log.info("Adding guests for booking with id: {}",bookingId);

        Booking booking=bookingRepository.findById(bookingId).orElseThrow(()->
                new ResourceNotFoundException("Booking not found with id: "+bookingId));

        if (hasBookinExpired(booking)){
            throw new IllegalStateException("Booking is expired");
        }

        if(booking.getBookingStatus()!=BookingStatus.RESERVED){
            throw new IllegalStateException("Booking is not under reserved state, cannot add guest");
        }


        for (GuestDto guestDto:guestDtoList){
            Guest guest=modeMapper.map(guestDto,Guest.class);
            guest.setUser(getCurrentUser());
            guest=guestRepository.save(guest);
            booking.getGuests().add(guest);
        }
        booking.setBookingStatus(BookingStatus.GUEST_ADDED);
        booking=bookingRepository.save(booking);

        return modeMapper.map(booking, BookingDTO.class);

    }

    public User getCurrentUser(){
        User user=new User();
        user.setId(1L);
        return user;
    }


    public Boolean hasBookinExpired(Booking booking){
        return booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
    }
}
