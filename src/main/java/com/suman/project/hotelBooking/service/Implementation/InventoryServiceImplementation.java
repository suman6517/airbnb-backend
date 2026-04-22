package com.suman.project.hotelBooking.service.Implementation;

import com.suman.project.hotelBooking.dto.HotelDto;
import com.suman.project.hotelBooking.dto.HotelPriceDto;
import com.suman.project.hotelBooking.dto.HotelSearchRequestDto;
import com.suman.project.hotelBooking.entity.Hotel;
import com.suman.project.hotelBooking.entity.Inventory;
import com.suman.project.hotelBooking.entity.Room;
import com.suman.project.hotelBooking.repository.HotelMinPriceRepository;
import com.suman.project.hotelBooking.repository.InventoryRepository;
import com.suman.project.hotelBooking.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImplementation implements InventoryService
{
    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;
    private final HotelMinPriceRepository hotelMinPriceRepository;

    /**
     * Creates and persists daily Inventory records for the given room for the next year.
     *
     * Each Inventory covers one calendar day from today through the same date one year from now (inclusive)
     * and is initialized with zero bookings/reservations, the room's price and total count, surge factor of 1,
     * the room's hotel and city, and marked open (not closed).
     *
     * @param room the Room to initialize inventory for
     */
    @Override
    public void initializeRoomForAYear(Room room)
    {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusYears(1);
        for(; !today.isAfter(endDate) ; today = today.plusDays(1))
        {
            Inventory inventory = Inventory.builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .bookCount(0)
                    .reservedCount(0)
                    .city(room.getHotel().getCity())
                    .date(today)
                    .price(room.getPrice())
                    .surgeFactor(BigDecimal.ONE)
                    .totalCount(room.getTotalCount())
                    .closed(false)
                    .build();

            inventoryRepository.save(inventory);
        }

    }

    /**
     * Deletes all Inventory records associated with the specified Room.
     *
     * @param room the Room whose inventories will be removed
     */
    @Override
    public void deleteAllInventoryes(Room room)
    {
        log.info("Delete all inventory's of Room with id {}", room.getId());

        inventoryRepository.deleteByAndRoom(room);
    }

    /**
     * Searches hotels that have available inventory for the requested date range and returns their minimum prices.
     *
     * @param hotelSearchRequestDto search criteria containing city, start and end dates, requested room count, page, and page size
     * @return a page of {@code HotelPriceDto} entries for hotels that have availability for every night in the requested range; each entry includes the hotel's minimum price information for that range
     */
    @Override
    public Page<HotelPriceDto> searchHotels(HotelSearchRequestDto hotelSearchRequestDto)
    {
        log.info("Searching hotels for {} city , from {} to {}", hotelSearchRequestDto.getCity(), hotelSearchRequestDto.getStartDate(), hotelSearchRequestDto.getEndDate());
        Pageable pageable =  PageRequest.of(hotelSearchRequestDto.getPage(), hotelSearchRequestDto.getPageSize());

        long dateCount = ChronoUnit.DAYS.between(hotelSearchRequestDto.getStartDate() , hotelSearchRequestDto.getEndDate())+1;

        // business logic - 90 days
        Page<HotelPriceDto> hotelPage =
                hotelMinPriceRepository.findHotelsWithAvailableInventory(hotelSearchRequestDto.getCity() , hotelSearchRequestDto.getStartDate() ,
                hotelSearchRequestDto.getEndDate(),hotelSearchRequestDto.getRoomCount() , dateCount , pageable);


        return hotelPage;
    }
}
