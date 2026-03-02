package com.suman.project.hotelBooking.service;

import com.suman.project.hotelBooking.entity.Room;

public interface InventoryService
{
    void initializeRoomForAYear(Room room);

    void deleteAllInventoryes(Room room);


}
