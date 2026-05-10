package com.suman.project.hotelBooking.service.Implementation;

import com.suman.project.hotelBooking.dto.HotelPriceDto;
import com.suman.project.hotelBooking.dto.HotelSearchRequestDto;
import com.suman.project.hotelBooking.dto.InventoryDto;
import com.suman.project.hotelBooking.dto.UpdateInventoryRequestDto;
import com.suman.project.hotelBooking.entity.Inventory;
import com.suman.project.hotelBooking.entity.Room;
import com.suman.project.hotelBooking.entity.User;
import com.suman.project.hotelBooking.exception.ResourceNotFoundException;
import com.suman.project.hotelBooking.repository.HotelMinPriceRepository;
import com.suman.project.hotelBooking.repository.InventoryRepository;
import com.suman.project.hotelBooking.repository.RoomRepository;
import com.suman.project.hotelBooking.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.suman.project.hotelBooking.utility.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImplementation implements InventoryService
{
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;
    private final HotelMinPriceRepository hotelMinPriceRepository;

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

    @Override
    public void deleteAllInventoryes(Room room)
    {
        log.info("Delete all inventory's of Room with id {}", room.getId());

        inventoryRepository.deleteByAndRoom(room);
    }

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

    @Override
    public List<InventoryDto> getAllInventoryByRoom(Long roomId)
    {
        log.info("Getting inventory for room with id {}", roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room with id " + roomId + " not found"));

        User user = getCurrentUser();
        if(!user.equals(room.getHotel().getOwner()))
        {
            throw new AccessDeniedException("You are not the owner of this room with id " + roomId);
        }

        return inventoryRepository.findByRoomOrderByDate(room).stream()
                .map((element) -> modelMapper.map(element, InventoryDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateInventory(Long roomId, UpdateInventoryRequestDto updateInventoryRequestDto)
    {
        log.info("Updating all the inventory's by room for room id {} between date range: {} - {}", roomId ,
                updateInventoryRequestDto.getStartDate(), updateInventoryRequestDto.getEndDate());
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room with id " + roomId + " not found"));

        User user = getCurrentUser();
        if(!user.equals(room.getHotel().getOwner()))
        {
            throw new AccessDeniedException("You are not the owner of this room with id " + roomId);
        }

        inventoryRepository.getInventoryAndLockBeforeUpdate(roomId ,
                updateInventoryRequestDto.getStartDate() ,
                updateInventoryRequestDto.getEndDate());

        inventoryRepository.updateInventory(roomId ,
                updateInventoryRequestDto.getStartDate(),
                updateInventoryRequestDto.getEndDate() ,
                updateInventoryRequestDto.getClosed() ,
                updateInventoryRequestDto.getSurgeFactor());

    }
}
