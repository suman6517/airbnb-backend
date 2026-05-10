package com.suman.project.hotelBooking.service;

import com.suman.project.hotelBooking.dto.*;
import com.suman.project.hotelBooking.entity.Room;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InventoryService
{
    void initializeRoomForAYear(Room room);

    void deleteAllInventoryes(Room room);


    Page<HotelPriceDto> searchHotels(HotelSearchRequestDto hotelSearchRequestDto);

    List<InventoryDto> getAllInventoryByRoom(Long roomId);

    void updateInventory(Long roomId, UpdateInventoryRequestDto updateInventoryRequestDto);
}
