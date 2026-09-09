package com.example.airBnbApp.service;

import com.example.airBnbApp.dto.HotelDto;
import com.example.airBnbApp.dto.HotelPriceDto;
import com.example.airBnbApp.dto.HotelSearchRequest;
import com.example.airBnbApp.entity.Room;
import org.springframework.data.domain.Page;

public interface InventoryService {
    void intializeRoomForYear(Room room);

    void deleteFutureInverntories(Room room);

    Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest);
}
