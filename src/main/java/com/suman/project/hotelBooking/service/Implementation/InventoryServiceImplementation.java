package com.suman.project.hotelBooking.service.Implementation;

import com.suman.project.hotelBooking.entity.Inventory;
import com.suman.project.hotelBooking.entity.Room;
import com.suman.project.hotelBooking.repository.InventoryRepository;
import com.suman.project.hotelBooking.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImplementation implements InventoryService
{
    private final InventoryRepository inventoryRepository;

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

    @Override
    public void deleteAllInventoryes(Room room) {

        inventoryRepository.deleteByAndRoom(room);
    }
}
