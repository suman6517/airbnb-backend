package com.suman.project.hotelBooking.service.Implementation;

import com.suman.project.hotelBooking.dto.RoomDto;
import com.suman.project.hotelBooking.entity.Hotel;
import com.suman.project.hotelBooking.entity.Room;
import com.suman.project.hotelBooking.exception.ResourceNotFoundException;
import com.suman.project.hotelBooking.repository.HotelRepository;
import com.suman.project.hotelBooking.repository.RoomRepository;
import com.suman.project.hotelBooking.service.InventoryService;
import com.suman.project.hotelBooking.service.RoomService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImplimentation implements RoomService
{
    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;
    private final HotelRepository hotelRepository;
    private final InventoryService inventoryService;

    @Override
    public RoomDto createNewRoom(RoomDto roomDto, Long hotelId)
    {

        log.info("Creating a new room in hotel with id {}",hotelId);

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() ->new ResourceNotFoundException("Hotel with Id " + hotelId + " not found"));

        Room room = modelMapper.map(roomDto, Room.class);

        room.setHotel(hotel);
        room = roomRepository.save(room);
        if(hotel.getActive())
        {
            inventoryService.initializeRoomForAYear(room);
        }
        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    public List<RoomDto> getAllRoomsInTheHotel(Long hotelId)
    {
        log.info("Getting all rooms in Hotel with id {}",hotelId);

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() ->new ResourceNotFoundException("Hotel with Id " + hotelId + " not found"));


        return hotel.getRooms()
                .stream()
                .map((element) -> modelMapper.map(element , RoomDto.class)).collect(Collectors.toList());
    }

    @Override
    public RoomDto getRoomById(Long roomId)
    {

        log.info("Getting the room with id {}",roomId);

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() ->new ResourceNotFoundException("Room with Id " + roomId + " not found"));


        return modelMapper.map(room, RoomDto.class);
    }

    @Transactional
    @Override
    public void deleteRoomById(Long roomId)
    {
        log.info("Deleting the room with id {}",roomId);

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() ->new ResourceNotFoundException("Room with Id " + roomId + " not found"));

        inventoryService.deleteAllInventoryes(room);
        roomRepository.deleteById(roomId);



    }
}
