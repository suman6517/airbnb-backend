package com.suman.project.hotelBooking.service;

import com.suman.project.hotelBooking.dto.HotelDto;
import com.suman.project.hotelBooking.dto.HotelPriceDto;
import com.suman.project.hotelBooking.dto.HotelSearchRequestDto;
import com.suman.project.hotelBooking.entity.Room;
import org.springframework.data.domain.Page;

public interface InventoryService
{
    void initializeRoomForAYear(Room room);

    /**
 * Deletes all inventory records associated with the specified room.
 *
 * @param room the room whose inventory entries should be removed
 */
void deleteAllInventoryes(Room room);


    /**
 * Searches for hotels that match the provided criteria and returns paginated price results.
 *
 * @param hotelSearchRequestDto the search criteria (e.g., location, check-in/check-out dates, occupancy, and filters)
 * @return a Page of HotelPriceDto containing hotels that match the criteria along with their pricing information; the page may be empty if no matches
 */
Page<HotelPriceDto> searchHotels(HotelSearchRequestDto hotelSearchRequestDto);
}
