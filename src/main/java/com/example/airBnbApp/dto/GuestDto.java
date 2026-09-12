package com.example.airBnbApp.dto;

import com.example.airBnbApp.entity.Booking;
import com.example.airBnbApp.entity.User;
import com.example.airBnbApp.entity.enums.Gender;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class GuestDto {

    private Long id;

    private User user;

    private String name;

    private LocalDateTime createdAt;

    private Gender gender;

    private Integer age;

    private Set<Booking> bookings;
}
