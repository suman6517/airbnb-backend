package com.suman.project.hotelBooking.service.Implementation;

import com.suman.project.hotelBooking.dto.HotelDto;
import com.suman.project.hotelBooking.dto.HotelInfoDto;
import com.suman.project.hotelBooking.dto.RoomDto;
import com.suman.project.hotelBooking.entity.Hotel;
import com.suman.project.hotelBooking.entity.Room;
import com.suman.project.hotelBooking.exception.ResourceNotFoundException;
import com.suman.project.hotelBooking.repository.HotelRepository;
import com.suman.project.hotelBooking.repository.RoomRepository;
import com.suman.project.hotelBooking.service.HotelService;
import com.suman.project.hotelBooking.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImplementation implements HotelService
{

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;
    private final RoomRepository roomRepository;
    @Override
    public HotelDto createHotel(HotelDto hotelDto)
    {
        log.info("Creating a new Hotel with name {}", hotelDto.getName());
        Hotel hotel = modelMapper.map(hotelDto, Hotel.class);
        hotel.setActive(false);
        hotel = hotelRepository.save(hotel);
        HotelDto createHotelDto = modelMapper.map(hotel, HotelDto.class);
        log.info("Created a new Hotel with Id {}", createHotelDto.getId());
        return createHotelDto;
    }

    @Override
    public HotelDto getHotelById(Long id)
    {
        log.info("Getting a  Hotel with Id {}", id);

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("Hotel with Id " + id + " not found"));

        return  modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto updateHotel(HotelDto hotelDto, Long id)
    {
        log.info("Updating a  Hotel with Id {}", id);

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("Hotel with Id " + id + " not found"));

        modelMapper.map(hotelDto, hotel);
        hotel.setId(id);
        hotel = hotelRepository.save(hotel);
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    @Transactional
    public Boolean deleteHotelById(Long id)
    {

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("Hotel with Id " + id + " not found"));
        hotelRepository.deleteById(id);
        for(Room room : hotel.getRooms())
        {
            inventoryService.deleteAllInventoryes(room);
            roomRepository.deleteById(room.getId());
        }

        return true;
    }

    @Override
    @Transactional
    public void activateHotel(Long id)
    {
        log.info("Activating a  Hotel with Id {}", id);

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("Hotel with Id " + id + " not found"));

        hotel.setActive(true);

        // Assuming Only do it once
        for(Room room : hotel.getRooms())
        {
            inventoryService.initializeRoomForAYear(room);
        }
    }

    @Override
    public HotelInfoDto getHotelInfoById(Long hotelId)
    {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() ->new ResourceNotFoundException("Hotel with Id " + hotelId + " not found"));

        List<RoomDto> rooms = hotel.getRooms()
                .stream()
                .map((element) -> modelMapper.map(element , RoomDto.class))
                .toList();

        return new HotelInfoDto(modelMapper.map(hotel, HotelDto.class), rooms);
    }
}
