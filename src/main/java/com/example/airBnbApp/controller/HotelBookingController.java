package com.example.airBnbApp.controller;

import com.example.airBnbApp.dto.BookingDTO;
import com.example.airBnbApp.dto.BookingRequest;
import com.example.airBnbApp.dto.GuestDto;
import com.example.airBnbApp.service.BookingService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class HotelBookingController {

    private final BookingService bookingService;

    @PostMapping("/init")
    public ResponseEntity<BookingDTO>initialiseBooking(@RequestBody BookingRequest bookingRequest){
        return ResponseEntity.ok(bookingService.initialiseBooking(bookingRequest));
    }

    @PostMapping("/{bookingId}/addGuest")
    public ResponseEntity<BookingDTO>addGuest(@PathVariable Long bookingId, @RequestBody List<GuestDto>guestDtoList){
        return ResponseEntity.ok(bookingService.addGuests(bookingId,guestDtoList));
    }
}
