package com.suman.project.hotelBooking.service;

import com.suman.project.hotelBooking.dto.HotelDto;
import com.suman.project.hotelBooking.dto.HotelSearchRequestDto;
import com.suman.project.hotelBooking.entity.Room;
import org.springframework.data.domain.Page;

public interface InventoryService
{
    void initializeRoomForAYear(Room room);

    void deleteAllInventoryes(Room room);


    Page<HotelDto> searchHotels(HotelSearchRequestDto hotelSearchRequestDto);
}
