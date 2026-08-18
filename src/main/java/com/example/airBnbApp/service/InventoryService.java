package com.example.airBnbApp.service;

import com.example.airBnbApp.entity.Room;

public interface InventoryService {
    void intializeRoomForYear(Room room);

    void deleteFutureInverntories(Room room);
}
