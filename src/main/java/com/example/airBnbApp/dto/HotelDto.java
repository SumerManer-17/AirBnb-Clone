package com.example.airBnbApp.dto;

import com.example.airBnbApp.entity.HotelContactInfo;
import com.example.airBnbApp.entity.Room;

import lombok.Data;

import java.util.List;
@Data
public class HotelDto {

    private Long id;

    private String name;

    private String city;

    private String[] photos;

    private String[] amenities;

    private HotelContactInfo hotelContactInfo;

    private Boolean active;

    //private List<Room> rooms;
}
