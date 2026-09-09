package com.example.airBnbApp.service;

import com.example.airBnbApp.dto.BookingDTO;
import com.example.airBnbApp.dto.BookingRequest;
import com.example.airBnbApp.dto.GuestDto;

import java.util.List;

public interface BookingService {

    BookingDTO initialiseBooking(BookingRequest bookingRequest);

    BookingDTO addGuests(Long bookingId, List<GuestDto> guestDtoList);
}
