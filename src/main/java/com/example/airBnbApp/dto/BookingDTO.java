package com.example.airBnbApp.dto;

import com.example.airBnbApp.entity.Hotel;
import com.example.airBnbApp.entity.Room;
import com.example.airBnbApp.entity.User;
import com.example.airBnbApp.entity.enums.BookingStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
@Data
public class BookingDTO {

    private Long id;

    private Integer roomsCount;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private LocalDateTime createdAt;

    private  LocalDateTime updatedAt;

    private BookingStatus bookingStatus;

    private Set<GuestDto> guests;
}
